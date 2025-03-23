package com.myapt.domain.news.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

public enum NewsPlatform {
	YNA("001", "연합뉴스"),
	PRESSIAN("002", "프레시안"),
	NEWSIS("003", "뉴시스"),
	KMIB("005", "국민일보"),
	MEDIATODAY("006", "미디어오늘"),
	MT("008", "머니투데이"),
	MK("009", "매일경제"),
	SEDAILY("011", "서울경제"),
	FNNEWS("014", "파이낸셜뉴스"),
	HANKYUNG("015", "한국경제"),
	HERALD_CORP("016", "헤럴드경제"),
	EDAILY("018", "이데일리"),
	DONGA("020", "동아일보"),
	MUNHWA("021", "문화일보"),
	SEGYE("022", "세계일보"),
	CHOSUN("023", "조선일보"),
	MK_ECONOMY("024", "매경이코노미"),
	JOONGANG("025", "중앙일보"),
	HANI("028", "한겨레"),
	DT("029", "디지털타임스"),
	ETNEWS("030", "전자신문"),
	INEWS24("031", "아이뉴스24"),
	KHAN("032", "경향신문"),
	HANI_21("036", "한겨례21"),
	WEEKLY_DONGA("037", "주간동아"),
	OHMY_NEWS("047", "오마이뉴스"),
	HANKYUNG_BUSINESS("050", "한경비즈니스"),
	YTN("052", "YTN"),
	WEEKLY_CHOSUN("053", "주간조선"),
	SBS("055", "SBS"),
	KBS("056", "KBS"),
	MBN("057", "MBN"),
	NOCUT_NEWS("079", "노컷뉴스"),
	SEOUL_NEWS("081", "서울신문"),
	BUSAN_ILBO("082", "부산일보"),
	KWNEWS("087", "강원일보"),
	IMAEIL("088", "매일신문"),
	ZDNET_KOREA("092", "지디넷코리아"),
	STAR_NEWS("108", "스타뉴스"),
	MYDAILY("117", "마이데일리"),
	DAILIAN("119", "데일리안"),
	JOSE_ILBO("123", "조세일보"),
	DIGITAL_DAILY("138", "디지털데일리"),
	IMBC_MBC("214", "MBC"),
	WOWTV("215", "한국경제TV"),
	ISPLUS("241", "일간스포츠"),
	ECONOMIST("243", "이코노미스트"),
	SHINDONGA("262", "신동아"),
	ASIAE("277", "아시아경제"),
	BLOTER("293", "블로터"),
	WOMEN_NEWS("310", "여성신문"),
	JOONGANG_SUNDAY("353", "중앙SUNDAY"),
	CHOSUN_BIZ("366", "조선비즈"),
	SBS_BIZ("374", "SBS Biz"),
	MONEY_S("417", "머니S"),
	NEWS1("421", "뉴스1"),
	YONHAP_TV("422", "연합뉴스TV"),
	JTBC("437", "JTBC"),
	TV_CHOSUN("448", "TV조선"),
	CHANNEL_A("449", "채널A"),
	SPORTSSEOUL("468", "스포츠서울"),
	HANKOOK_ILBO("469", "한국일보"),
	SISAJOURNAL("586", "시사저널"),
	TF("629", "더팩트"),
	KOREA_CENTRAL_DAILY("640", "코리아중앙데일리"),
	BIZWATCH("648", "비즈워치"),
	KANGWON_MIN_ILBO("654", "강원도민일보"),
	DAEJEON_ILBO("656", "대전일보"),
	DAEGU_MBC("657", "대구MBC"),
	GUKJE_SINMUN("658", "국제신문"),
	JEONJU_MBC("659", "전주MBC"),
	KBC_GWANGJU("660", "kbc광주방송"),
	JIBS("661", "JIBS"),
	THE_SCOOP("665", "더스쿠프"),
	KYEONGGI("666", "경기일보");

	private final String code;
	@Getter
	private final String name;

	NewsPlatform(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<NewsPlatform> fromCode(String code) {
		return Arrays.stream(NewsPlatform.values())
			.filter(source -> source.code.equals(code))
			.findFirst();
	}
}
