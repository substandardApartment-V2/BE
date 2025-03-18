package com.myapt.domain.apt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {
    private static final Logger log = LoggerFactory.getLogger(CsvServiceImpl.class);
    private final AvgPricesRepository avgPricesRepository;
    private final PlannedAptsRepository plannedAptsRepository;

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    @Scheduled(cron = "0 0 0 1 * ?") // 매달 1일 자정 실행
    public void scheduledDownloadAndSaveCsvData() { //@Scheduled로 주기적으로 실행되며, 외부 API에서 데이터를 가져와 데이터베이스에 저장.
        try {
            String url = baseUrl + endpoint + "?serviceKey=" + serviceKey + "&returnType=JSON";
            Map<String, Long> dateCountMap = fetchAllData(url);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            System.err.println("Scheduled task failed: " + e.getMessage()); // 로깅 프레임워크 사용 권장
        }
    }

    @Override
    public void downloadAndSaveCsvDataV2(String baseUrl) {
        try {
            Map<String, Long> dateCountMap = fetchAllData(baseUrl);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download and save planned apts data: " + e.getMessage(), e);
        }
    }

    //외부 API에서 JSON 데이터를 가져와 Map<String, Long>으로 변환.
    private Map<String, Long> fetchAllData(String baseUrl) throws Exception {
        Map<String, Long> dateCountMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int page = 1;
        int perPage = 100;
        int totalCount;

        do {
            String url = baseUrl + "&page=" + page + "&perPage=" + perPage;
            try (InputStream inputStream = downloadFromUrl(url)) {
                JsonNode root = mapper.readTree(inputStream);
                totalCount = root.path("totalCount").asInt(0);
                JsonNode dataArray = root.path("data");

                if (!dataArray.isArray() || dataArray.size() == 0) {
                    System.out.println("No data found for page " + page);
                    break;
                }

                for (JsonNode node : dataArray) {
                    String scheduledMonth = node.path("연월").asText(null);
                    if (scheduledMonth == null || scheduledMonth.isEmpty()) {
                        System.out.println("Skipping invalid scheduledMonth: " + node.toString());
                        continue;
                    }

                    String[] yearMonth = scheduledMonth.split("-");
                    if (yearMonth.length != 2) {
                        System.out.println("Invalid format for scheduledMonth: " + scheduledMonth);
                        continue;
                    }

                    String key = scheduledMonth; // "YYYY-MM"
                    dateCountMap.merge(key, 1L, Long::sum);
                }
            }
            page++;
        } while ((page - 1) * perPage < totalCount);

        return dateCountMap;
    }

    //URL에서 데이터를 다운로드하는 헬퍼 메서드.
    private InputStream downloadFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API call failed: HTTP " + responseCode);
        }
        return connection.getInputStream();
    }

    //데이터를 데이터베이스에 저장.
    @Transactional
    private void saveDataToDatabase(Map<String, Long> dateCountMap) {
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            String[] yearMonth = entry.getKey().split("-");
            long year = Long.parseLong(yearMonth[0]);
            long month = Long.parseLong(yearMonth[1]);
            long count = entry.getValue();

            Optional<PlannedApts> existing = plannedAptsRepository.findByYearAndMonth(year, month);
            PlannedApts plannedApts = existing.orElse(new PlannedApts());
            plannedApts.setYear(year);
            plannedApts.setMonth(month);
            plannedApts.setCount(count);
            plannedAptsRepository.save(plannedApts);
        }
    }

    // 전국 아파트 평균 매매가 다운로드 및 DB 저장
    @Override
    @Transactional
    public void downloadAndSaveAveragePriceData(String targetUrl) {
        WebDriver webDriver = null;
        try {
            // WebDriverManager 캐시 경로 설정 (도커 환경에서 영구화된 볼륨 사용)
            System.setProperty("wdm.cachePath", "/webdriver-cache");

            // ChromeDriver를 자동으로 설치하고 설정
            int retries = 3;
            boolean driverSetupSuccess = false;
            for (int i = 0; i < retries; i++) {
                try {
                    WebDriverManager.chromedriver().setup();
                    driverSetupSuccess = true;
                    break;
                } catch (Exception e) {
                    log.warn("Failed to setup ChromeDriver, retrying {}/{}: {}", i + 1, retries, e.getMessage());
                    Thread.sleep(2000); // 2초 대기 후 재시도
                }
            }
            if (!driverSetupSuccess) {
                throw new RuntimeException("Failed to setup ChromeDriver after " + retries + " attempts");
            }

            // 시스템의 기본 임시 디렉토리를 동적으로 가져오기
            String tempDir = System.getProperty("java.io.tmpdir");
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", tempDir);
            chromePrefs.put("download.prompt_for_download", false);

            // Chrome 옵션 설정: 화면 없이 실행하고 Docker 환경 최적화
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--headless");
            options.addArguments("--no-sandbox"); // Docker 환경에서 필요
            options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 문제 해결
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-blink-features=AutomationControlled");

            // 고유한 사용자 데이터 디렉토리 생성
            Path userDataDir = Files.createTempDirectory("chrome-user-data-");
            options.addArguments("--user-data-dir=" + userDataDir.toString());

            // Chrome 브라우저 실행
            webDriver = new ChromeDriver(options);
            log.info("ChromeDriver 초기화 완료");

            // 웹사이트에서 엑셀 파일 다운로드
            downloadCsvFromWebsite(webDriver, targetUrl);

            // 다운로드된 파일 경로 설정
            String downloadPath = tempDir;
            Path downloadedFile = findLatestFileWithWait(downloadPath);

            // 파일이 정상적으로 다운로드되었는지 확인
            if (downloadedFile != null && Files.exists(downloadedFile) && Files.size(downloadedFile) > 1024) {
                log.info("다운로드된 평균 가격 데이터 파일: {}", downloadedFile.toString());
                try (InputStream excelStream = Files.newInputStream(downloadedFile)) {
                    processAndSaveExcelData(excelStream, targetUrl);
                }
                Files.deleteIfExists(downloadedFile);
                log.info("임시 파일 삭제 완료: {}", downloadedFile.toString());
            } else {
                throw new RuntimeException("평균 가격 XLSX 파일 다운로드에 실패했거나 데이터가 비어 있습니다.");
            }
        } catch (Exception e) {
            log.error("평균 가격 데이터 다운로드 및 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("평균 가격 데이터 다운로드 및 저장 중 오류 발생: " + e.getMessage(), e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
                log.info("WebDriver 종료 완료");
            }
        }
    }

    // 스케줄링된 크롤링 실행
    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정 (0시 0분 0초)
    public void scheduledDownloadAndSaveAveragePriceData() {
        log.info("스케줄링된 크롤링 시작 - 매월 1일 자정");
        String targetUrl = "https://data.kbland.kr/kbstats/wmh?tIdx=HT06&tsIdx=aptSaleAvgPrice";
        try {
            downloadAndSaveAveragePriceData(targetUrl);
            log.info("스케줄링된 크롤링 완료");
        } catch (Exception e) {
            log.error("스케줄링된 크롤링 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    // 웹사이트에서 엑셀 파일을 다운로드하는 메서드
    private void downloadCsvFromWebsite(WebDriver webDriver, String targetUrl) {
        log.info("웹사이트 접속: {}", targetUrl);
        webDriver.get(targetUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.bodyitem")));
        log.debug("bodyitem 요소 로드 완료");

        WebElement moreButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn-dotmore.black.iconbtn")));
        moreButton.click();
        log.debug("'더보기' 버튼 클릭 완료");

        WebElement excelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.optionLayer.active li.excel")));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", excelButton);
        log.info("Excel 다운로드 버튼 클릭 완료");

        try {
            Thread.sleep(10000); // 다운로드 완료 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("다운로드 대기 중 인터럽트 발생", e);
        }
    }

    // 다운로드 폴더에서 가장 최근 엑셀 파일을 찾는 메서드
    private Path findLatestFileWithWait(String directory) throws Exception {
        int maxWaitSeconds = 30;
        int waitedSeconds = 0;
        Path downloadedFile = null;

        while (waitedSeconds < maxWaitSeconds) {
            downloadedFile = Files.list(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xlsx"))
                    .max((p1, p2) -> Long.compare(p1.toFile().lastModified(), p2.toFile().lastModified()))
                    .orElse(null);

            if (downloadedFile != null && Files.exists(downloadedFile) && Files.size(downloadedFile) > 1024) {
                break;
            }
            Thread.sleep(1000);
            waitedSeconds++;
        }
        return downloadedFile;
    }

    // 엑셀 파일을 읽고 DB에 저장하는 메서드
    @Transactional
    private void processAndSaveExcelData(InputStream excelStream, String targetUrl) throws Exception {
        log.info("Excel 데이터 파싱 시작");
        try (Workbook workbook = new XSSFWorkbook(excelStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            Map<Integer, String> columnDateMap = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            for (int col = 1; col < headerRow.getLastCellNum(); col++) {
                Cell cell = headerRow.getCell(col);
                String yearMonth;
                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    yearMonth = dateFormat.format(date);
                } else {
                    yearMonth = cell.getStringCellValue().substring(0, 7);
                }
                columnDateMap.put(col, yearMonth);
            }

            Row dataRow = sheet.getRow(1);
            if (dataRow == null) {
                throw new RuntimeException("두 번째 행(전국 데이터)이 비어 있습니다.");
            }

            for (int col = 1; col < dataRow.getLastCellNum(); col++) {
                String yearMonth = columnDateMap.get(col);
                if (yearMonth == null) continue;

                String[] yearMonthSplit = yearMonth.split("-");
                if (yearMonthSplit.length != 2) continue;

                long year = Long.parseLong(yearMonthSplit[0]);
                long month = Long.parseLong(yearMonthSplit[1]);

                Cell cell = dataRow.getCell(col);
                if (cell == null) continue;

                Long avgPrice;
                switch (cell.getCellType()) {
                    case NUMERIC:
                        double numericValue = cell.getNumericCellValue();
                        avgPrice = Math.round(numericValue);
                        break;
                    case STRING:
                        String cellValue = cell.getStringCellValue().trim();
                        if (cellValue.equals("-")) continue;
                        avgPrice = Math.round(Double.parseDouble(cellValue));
                        break;
                    default:
                        log.debug("처리되지 않은 셀 타입: {}, 위치: [1, {}]", cell.getCellType(), col);
                        continue;
                }

                Optional<AvgPrices> existing = avgPricesRepository.findByYearAndMonth(year, month);
                AvgPrices avgPrices = existing.orElse(new AvgPrices());
                avgPrices.setYear(year);
                avgPrices.setMonth(month);
                avgPrices.setAvgPrice(avgPrice);
                avgPricesRepository.save(avgPrices);
            }
            log.info("Excel 데이터 파싱 및 MySQL DB 저장 완료");
        }
    }
}
