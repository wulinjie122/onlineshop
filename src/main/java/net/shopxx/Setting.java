package net.shopxx;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * …Ë÷√
 */
public class Setting implements Serializable {
	
	private static final long serialVersionUID = -1478999889661796840L;
	public static final String CACHE_NAME = "setting";

	public static enum WatermarkPosition {
		no, topLeft, topRight, center, bottomLeft, bottomRight;
	}

	public static enum RoundType {
		roundHalfUp, roundUp, roundDown;
	}

	public static enum CaptchaType {
		memberLogin, memberRegister, adminLogin, review, consultation, findPassword, resetPassword, other;
	}

	public static enum AccountLockType {
		member, admin;
	}

	public static enum StockAllocationTime {
		order, payment, ship;
	}

	public static enum ReviewAuthority {
		anyone, member, purchased;
	}

	public static enum ConsultationAuthority {
		anyone, member;
	}

	public static final Integer CACHE_KEY = Integer.valueOf(0);
	private static final String SEPARATOR = ",";
	private String siteName;
	private String siteUrl;
	private String logo;
	private String hotSearch;
	private String address;
	private String phone;
	private String zipCode;
	private String email;
	private String certtext;
	private Boolean isSiteEnabled;
	private String siteCloseMessage;
	private Integer largeProductImageWidth;
	private Integer largeProductImageHeight;
	private Integer mediumProductImageWidth;
	private Integer mediumProductImageHeight;
	private Integer thumbnailProductImageWidth;
	private Integer thumbnailProductImageHeight;
	private String defaultLargeProductImage;
	private String defaultMediumProductImage;
	private String defaultThumbnailProductImage;
	private Integer watermarkAlpha;
	private String watermarkImage;
	private WatermarkPosition watermarkPosition;
	private Integer priceScale;
	private RoundType priceRoundType;
	private Boolean isShowMarketPrice;
	private Double defaultMarketPriceScale;
	/* 84: */private Boolean isRegisterEnabled;
	/* 85: */private Boolean isDuplicateEmail;
	/* 86: */private String disabledUsername;
	/* 87: */private Integer usernameMinLength;
	/* 88: */private Integer usernameMaxLength;
	/* 89: */private Integer passwordMinLength;
	/* 90: */private Integer passwordMaxLength;
	/* 91: */private Long registerPoint;
	/* 92: */private String registerAgreement;
	/* 93: */private Boolean isEmailLogin;
	/* 94: */private CaptchaType[] captchaTypes;
	/* 95: */private AccountLockType[] accountLockTypes;
	/* 96: */private Integer accountLockCount;
	/* 97: */private Integer accountLockTime;
	/* 98: */private Integer safeKeyExpiryTime;
	/* 99: */private Integer uploadMaxSize;
	/* 100: */private String uploadImageExtension;
	/* 101: */private String uploadFlashExtension;
	/* 102: */private String uploadMediaExtension;
	/* 103: */private String uploadFileExtension;
	/* 104: */private String imageUploadPath;
	/* 105: */private String flashUploadPath;
	/* 106: */private String mediaUploadPath;
	/* 107: */private String fileUploadPath;
	/* 108: */private String smtpFromMail;
	/* 109: */private String smtpHost;
	/* 110: */private Integer smtpPort;
	/* 111: */private String smtpUsername;
	/* 112: */private String smtpPassword;
	/* 113: */private String currencySign;
	/* 114: */private String currencyUnit;
	/* 115: */private Integer stockAlertCount;
	/* 116: */private StockAllocationTime stockAllocationTime;
	/* 117: */private Double defaultPointScale;
	/* 118: */private Boolean isDevelopmentEnabled;
	/* 119: */private Boolean isReviewEnabled;
	/* 120: */private Boolean isReviewCheck;
	/* 121: */private ReviewAuthority reviewAuthority;
	/* 122: */private Boolean isConsultationEnabled;
	/* 123: */private Boolean isConsultationCheck;
	/* 124: */private ConsultationAuthority consultationAuthority;
	/* 125: */private Boolean isInvoiceEnabled;
	/* 126: */private Boolean isTaxPriceEnabled;
	/* 127: */private Double taxRate;
	/* 128: */private String cookiePath;
	/* 129: */private String cookieDomain;
	/* 130: */private String kuaidi100Key;
	/* 131: */private Boolean isCnzzEnabled;
	/* 132: */private String cnzzSiteId;
	/* 133: */private String cnzzPassword;

	/* 134: */
	/* 135: */@NotEmpty
	/* 136: */@Length(max = 200)
	/* 137: */public String getSiteName()
	/* 138: */{
		/* 139: 402 */return this.siteName;
		/* 140: */}

	/* 141: */
	/* 142: */public void setSiteName(String siteName)
	/* 143: */{
		/* 144: 412 */this.siteName = siteName;
		/* 145: */}

	/* 146: */
	/* 147: */@NotEmpty
	/* 148: */@Length(max = 200)
	/* 149: */public String getSiteUrl()
	/* 150: */{
		/* 151: 423 */return this.siteUrl;
		/* 152: */}

	/* 153: */
	/* 154: */public void setSiteUrl(String siteUrl)
	/* 155: */{
		/* 156: 433 */this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
		/* 157: */}

	/* 158: */
	/* 159: */@NotEmpty
	/* 160: */@Length(max = 200)
	/* 161: */public String getLogo()
	/* 162: */{
		/* 163: 444 */return this.logo;
		/* 164: */}

	/* 165: */
	/* 166: */public void setLogo(String logo)
	/* 167: */{
		/* 168: 454 */this.logo = logo;
		/* 169: */}

	/* 170: */
	/* 171: */@Length(max = 200)
	/* 172: */public String getHotSearch()
	/* 173: */{
		/* 174: 464 */return this.hotSearch;
		/* 175: */}

	/* 176: */
	/* 177: */public void setHotSearch(String hotSearch)
	/* 178: */{
		/* 179: 474 */if (hotSearch != null) {
			/* 180: 475 */hotSearch = hotSearch.replaceAll("[,\\s]*,[,\\s]*",
					",").replaceAll("^,|,$", "");
			/* 181: */}
		/* 182: 477 */this.hotSearch = hotSearch;
		/* 183: */}

	/* 184: */
	/* 185: */@Length(max = 200)
	/* 186: */public String getAddress()
	/* 187: */{
		/* 188: 487 */return this.address;
		/* 189: */}

	/* 190: */
	/* 191: */public void setAddress(String address)
	/* 192: */{
		/* 193: 497 */this.address = address;
		/* 194: */}

	/* 195: */
	/* 196: */@Length(max = 200)
	/* 197: */public String getPhone()
	/* 198: */{
		/* 199: 507 */return this.phone;
		/* 200: */}

	/* 201: */
	/* 202: */public void setPhone(String phone)
	/* 203: */{
		/* 204: 517 */this.phone = phone;
		/* 205: */}

	/* 206: */
	/* 207: */@Length(max = 200)
	/* 208: */public String getZipCode()
	/* 209: */{
		/* 210: 527 */return this.zipCode;
		/* 211: */}

	/* 212: */
	/* 213: */public void setZipCode(String zipCode)
	/* 214: */{
		/* 215: 537 */this.zipCode = zipCode;
		/* 216: */}

	/* 217: */
	/* 218: */@Email
	/* 219: */@Length(max = 200)
	/* 220: */public String getEmail()
	/* 221: */{
		/* 222: 548 */return this.email;
		/* 223: */}

	/* 224: */
	/* 225: */public void setEmail(String email)
	/* 226: */{
		/* 227: 558 */this.email = email;
		/* 228: */}

	/* 229: */
	/* 230: */@Length(max = 200)
	/* 231: */public String getCerttext()
	/* 232: */{
		/* 233: 568 */return this.certtext;
		/* 234: */}

	/* 235: */
	/* 236: */public void setCerttext(String certtext)
	/* 237: */{
		/* 238: 578 */this.certtext = certtext;
		/* 239: */}

	/* 240: */
	/* 241: */@NotNull
	/* 242: */public Boolean getIsSiteEnabled()
	/* 243: */{
		/* 244: 588 */return this.isSiteEnabled;
		/* 245: */}

	/* 246: */
	/* 247: */public void setIsSiteEnabled(Boolean isSiteEnabled)
	/* 248: */{
		/* 249: 598 */this.isSiteEnabled = isSiteEnabled;
		/* 250: */}

	/* 251: */
	/* 252: */@NotEmpty
	/* 253: */public String getSiteCloseMessage()
	/* 254: */{
		/* 255: 608 */return this.siteCloseMessage;
		/* 256: */}

	/* 257: */
	/* 258: */public void setSiteCloseMessage(String siteCloseMessage)
	/* 259: */{
		/* 260: 618 */this.siteCloseMessage = siteCloseMessage;
		/* 261: */}

	/* 262: */
	/* 263: */@NotNull
	/* 264: */@Min(1L)
	/* 265: */public Integer getLargeProductImageWidth()
	/* 266: */{
		/* 267: 629 */return this.largeProductImageWidth;
		/* 268: */}

	/* 269: */
	/* 270: */public void setLargeProductImageWidth(
			Integer largeProductImageWidth)
	/* 271: */{
		/* 272: 639 */this.largeProductImageWidth = largeProductImageWidth;
		/* 273: */}

	/* 274: */
	/* 275: */@NotNull
	/* 276: */@Min(1L)
	/* 277: */public Integer getLargeProductImageHeight()
	/* 278: */{
		/* 279: 650 */return this.largeProductImageHeight;
		/* 280: */}

	/* 281: */
	/* 282: */public void setLargeProductImageHeight(
			Integer largeProductImageHeight)
	/* 283: */{
		/* 284: 660 */this.largeProductImageHeight = largeProductImageHeight;
		/* 285: */}

	/* 286: */
	/* 287: */@NotNull
	/* 288: */@Min(1L)
	/* 289: */public Integer getMediumProductImageWidth()
	/* 290: */{
		/* 291: 671 */return this.mediumProductImageWidth;
		/* 292: */}

	/* 293: */
	/* 294: */public void setMediumProductImageWidth(
			Integer mediumProductImageWidth)
	/* 295: */{
		/* 296: 681 */this.mediumProductImageWidth = mediumProductImageWidth;
		/* 297: */}

	/* 298: */
	/* 299: */@NotNull
	/* 300: */@Min(1L)
	/* 301: */public Integer getMediumProductImageHeight()
	/* 302: */{
		/* 303: 692 */return this.mediumProductImageHeight;
		/* 304: */}

	/* 305: */
	/* 306: */public void setMediumProductImageHeight(
			Integer mediumProductImageHeight)
	/* 307: */{
		/* 308: 702 */this.mediumProductImageHeight = mediumProductImageHeight;
		/* 309: */}

	/* 310: */
	/* 311: */@NotNull
	/* 312: */@Min(1L)
	/* 313: */public Integer getThumbnailProductImageWidth()
	/* 314: */{
		/* 315: 713 */return this.thumbnailProductImageWidth;
		/* 316: */}

	/* 317: */
	/* 318: */public void setThumbnailProductImageWidth(
			Integer thumbnailProductImageWidth)
	/* 319: */{
		/* 320: 723 */this.thumbnailProductImageWidth = thumbnailProductImageWidth;
		/* 321: */}

	/* 322: */
	/* 323: */@NotNull
	/* 324: */@Min(1L)
	/* 325: */public Integer getThumbnailProductImageHeight()
	/* 326: */{
		/* 327: 734 */return this.thumbnailProductImageHeight;
		/* 328: */}

	/* 329: */
	/* 330: */public void setThumbnailProductImageHeight(
			Integer thumbnailProductImageHeight)
	/* 331: */{
		/* 332: 744 */this.thumbnailProductImageHeight = thumbnailProductImageHeight;
		/* 333: */}

	/* 334: */
	/* 335: */@NotEmpty
	/* 336: */@Length(max = 200)
	/* 337: */public String getDefaultLargeProductImage()
	/* 338: */{
		/* 339: 755 */return this.defaultLargeProductImage;
		/* 340: */}

	/* 341: */
	/* 342: */public void setDefaultLargeProductImage(
			String defaultLargeProductImage)
	/* 343: */{
		/* 344: 765 */this.defaultLargeProductImage = defaultLargeProductImage;
		/* 345: */}

	/* 346: */
	/* 347: */@NotEmpty
	/* 348: */@Length(max = 200)
	/* 349: */public String getDefaultMediumProductImage()
	/* 350: */{
		/* 351: 776 */return this.defaultMediumProductImage;
		/* 352: */}

	/* 353: */
	/* 354: */public void setDefaultMediumProductImage(
			String defaultMediumProductImage)
	/* 355: */{
		/* 356: 786 */this.defaultMediumProductImage = defaultMediumProductImage;
		/* 357: */}

	/* 358: */
	/* 359: */@NotEmpty
	/* 360: */@Length(max = 200)
	/* 361: */public String getDefaultThumbnailProductImage()
	/* 362: */{
		/* 363: 797 */return this.defaultThumbnailProductImage;
		/* 364: */}

	/* 365: */
	/* 366: */public void setDefaultThumbnailProductImage(
			String defaultThumbnailProductImage)
	/* 367: */{
		/* 368: 807 */this.defaultThumbnailProductImage = defaultThumbnailProductImage;
		/* 369: */}

	/* 370: */
	/* 371: */@NotNull
	/* 372: */@Min(0L)
	/* 373: */@Max(100L)
	/* 374: */public Integer getWatermarkAlpha()
	/* 375: */{
		/* 376: 819 */return this.watermarkAlpha;
		/* 377: */}

	/* 378: */
	/* 379: */public void setWatermarkAlpha(Integer watermarkAlpha)
	/* 380: */{
		/* 381: 829 */this.watermarkAlpha = watermarkAlpha;
		/* 382: */}

	/* 383: */
	/* 384: */public String getWatermarkImage()
	/* 385: */{
		/* 386: 838 */return this.watermarkImage;
		/* 387: */}

	/* 388: */
	/* 389: */public void setWatermarkImage(String watermarkImage)
	/* 390: */{
		/* 391: 848 */this.watermarkImage = watermarkImage;
		/* 392: */}

	/* 393: */
	/* 394: */@NotNull
	/* 395: */public WatermarkPosition getWatermarkPosition()
	/* 396: */{
		/* 397: 858 */return this.watermarkPosition;
		/* 398: */}

	/* 399: */
	/* 400: */public void setWatermarkPosition(
			WatermarkPosition watermarkPosition)
	/* 401: */{
		/* 402: 868 */this.watermarkPosition = watermarkPosition;
		/* 403: */}

	/* 404: */
	/* 405: */@NotNull
	/* 406: */@Min(0L)
	/* 407: */@Max(3L)
	/* 408: */public Integer getPriceScale()
	/* 409: */{
		/* 410: 880 */return this.priceScale;
		/* 411: */}

	/* 412: */
	/* 413: */public void setPriceScale(Integer priceScale)
	/* 414: */{
		/* 415: 890 */this.priceScale = priceScale;
		/* 416: */}

	/* 417: */
	/* 418: */@NotNull
	/* 419: */public RoundType getPriceRoundType()
	/* 420: */{
		/* 421: 900 */return this.priceRoundType;
		/* 422: */}

	/* 423: */
	/* 424: */public void setPriceRoundType(RoundType priceRoundType)
	/* 425: */{
		/* 426: 910 */this.priceRoundType = priceRoundType;
		/* 427: */}

	/* 428: */
	/* 429: */@NotNull
	/* 430: */public Boolean getIsShowMarketPrice()
	/* 431: */{
		/* 432: 920 */return this.isShowMarketPrice;
		/* 433: */}

	/* 434: */
	/* 435: */public void setIsShowMarketPrice(Boolean isShowMarketPrice)
	/* 436: */{
		/* 437: 930 */this.isShowMarketPrice = isShowMarketPrice;
		/* 438: */}

	/* 439: */
	/* 440: */@NotNull
	/* 441: */@Min(0L)
	/* 442: */@Digits(integer = 3, fraction = 3)
	/* 443: */public Double getDefaultMarketPriceScale()
	/* 444: */{
		/* 445: 942 */return this.defaultMarketPriceScale;
		/* 446: */}

	/* 447: */
	/* 448: */public void setDefaultMarketPriceScale(
			Double defaultMarketPriceScale)
	/* 449: */{
		/* 450: 952 */this.defaultMarketPriceScale = defaultMarketPriceScale;
		/* 451: */}

	/* 452: */
	/* 453: */@NotNull
	/* 454: */public Boolean getIsRegisterEnabled()
	/* 455: */{
		/* 456: 962 */return this.isRegisterEnabled;
		/* 457: */}

	/* 458: */
	/* 459: */public void setIsRegisterEnabled(Boolean isRegisterEnabled)
	/* 460: */{
		/* 461: 972 */this.isRegisterEnabled = isRegisterEnabled;
		/* 462: */}

	/* 463: */
	/* 464: */@NotNull
	/* 465: */public Boolean getIsDuplicateEmail()
	/* 466: */{
		/* 467: 982 */return this.isDuplicateEmail;
		/* 468: */}

	/* 469: */
	/* 470: */public void setIsDuplicateEmail(Boolean isDuplicateEmail)
	/* 471: */{
		/* 472: 992 */this.isDuplicateEmail = isDuplicateEmail;
		/* 473: */}

	/* 474: */
	/* 475: */@Length(max = 200)
	/* 476: */public String getDisabledUsername()
	/* 477: */{
		/* 478:1002 */return this.disabledUsername;
		/* 479: */}

	/* 480: */
	/* 481: */public void setDisabledUsername(String disabledUsername)
	/* 482: */{
		/* 483:1012 */if (disabledUsername != null) {
			/* 484:1013 */disabledUsername = disabledUsername.replaceAll(
					"[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
			/* 485: */}
		/* 486:1015 */this.disabledUsername = disabledUsername;
		/* 487: */}

	/* 488: */
	/* 489: */@NotNull
	/* 490: */@Min(1L)
	/* 491: */@Max(117L)
	/* 492: */public Integer getUsernameMinLength()
	/* 493: */{
		/* 494:1027 */return this.usernameMinLength;
		/* 495: */}

	/* 496: */
	/* 497: */public void setUsernameMinLength(Integer usernameMinLength)
	/* 498: */{
		/* 499:1037 */this.usernameMinLength = usernameMinLength;
		/* 500: */}

	/* 501: */
	/* 502: */@NotNull
	/* 503: */@Min(1L)
	/* 504: */@Max(117L)
	/* 505: */public Integer getUsernameMaxLength()
	/* 506: */{
		/* 507:1049 */return this.usernameMaxLength;
		/* 508: */}

	/* 509: */
	/* 510: */public void setUsernameMaxLength(Integer usernameMaxLength)
	/* 511: */{
		/* 512:1059 */this.usernameMaxLength = usernameMaxLength;
		/* 513: */}

	/* 514: */
	/* 515: */@NotNull
	/* 516: */@Min(1L)
	/* 517: */@Max(117L)
	/* 518: */public Integer getPasswordMinLength()
	/* 519: */{
		/* 520:1071 */return this.passwordMinLength;
		/* 521: */}

	/* 522: */
	/* 523: */public void setPasswordMinLength(Integer passwordMinLength)
	/* 524: */{
		/* 525:1081 */this.passwordMinLength = passwordMinLength;
		/* 526: */}

	/* 527: */
	/* 528: */@NotNull
	/* 529: */@Min(1L)
	/* 530: */@Max(117L)
	/* 531: */public Integer getPasswordMaxLength()
	/* 532: */{
		/* 533:1093 */return this.passwordMaxLength;
		/* 534: */}

	/* 535: */
	/* 536: */public void setPasswordMaxLength(Integer passwordMaxLength)
	/* 537: */{
		/* 538:1103 */this.passwordMaxLength = passwordMaxLength;
		/* 539: */}

	/* 540: */
	/* 541: */@NotNull
	/* 542: */@Min(0L)
	/* 543: */public Long getRegisterPoint()
	/* 544: */{
		/* 545:1114 */return this.registerPoint;
		/* 546: */}

	/* 547: */
	/* 548: */public void setRegisterPoint(Long registerPoint)
	/* 549: */{
		/* 550:1124 */this.registerPoint = registerPoint;
		/* 551: */}

	/* 552: */
	/* 553: */@NotEmpty
	/* 554: */public String getRegisterAgreement()
	/* 555: */{
		/* 556:1134 */return this.registerAgreement;
		/* 557: */}

	/* 558: */
	/* 559: */public void setRegisterAgreement(String registerAgreement)
	/* 560: */{
		/* 561:1144 */this.registerAgreement = registerAgreement;
		/* 562: */}

	/* 563: */
	/* 564: */@NotNull
	/* 565: */public Boolean getIsEmailLogin()
	/* 566: */{
		/* 567:1154 */return this.isEmailLogin;
		/* 568: */}

	/* 569: */
	/* 570: */public void setIsEmailLogin(Boolean isEmailLogin)
	/* 571: */{
		/* 572:1164 */this.isEmailLogin = isEmailLogin;
		/* 573: */}

	/* 574: */
	/* 575: */public CaptchaType[] getCaptchaTypes()
	/* 576: */{
		/* 577:1173 */return this.captchaTypes;
		/* 578: */}

	/* 579: */
	/* 580: */public void setCaptchaTypes(CaptchaType[] captchaTypes)
	/* 581: */{
		/* 582:1183 */this.captchaTypes = captchaTypes;
		/* 583: */}

	/* 584: */
	/* 585: */public AccountLockType[] getAccountLockTypes()
	/* 586: */{
		/* 587:1192 */return this.accountLockTypes;
		/* 588: */}

	/* 589: */
	/* 590: */public void setAccountLockTypes(AccountLockType[] accountLockTypes)
	/* 591: */{
		/* 592:1202 */this.accountLockTypes = accountLockTypes;
		/* 593: */}

	/* 594: */
	/* 595: */@NotNull
	/* 596: */@Min(1L)
	/* 597: */public Integer getAccountLockCount()
	/* 598: */{
		/* 599:1213 */return this.accountLockCount;
		/* 600: */}

	/* 601: */
	/* 602: */public void setAccountLockCount(Integer accountLockCount)
	/* 603: */{
		/* 604:1223 */this.accountLockCount = accountLockCount;
		/* 605: */}

	/* 606: */
	/* 607: */@NotNull
	/* 608: */@Min(0L)
	/* 609: */public Integer getAccountLockTime()
	/* 610: */{
		/* 611:1234 */return this.accountLockTime;
		/* 612: */}

	/* 613: */
	/* 614: */public void setAccountLockTime(Integer accountLockTime)
	/* 615: */{
		/* 616:1244 */this.accountLockTime = accountLockTime;
		/* 617: */}

	/* 618: */
	/* 619: */@NotNull
	/* 620: */@Min(0L)
	/* 621: */public Integer getSafeKeyExpiryTime()
	/* 622: */{
		/* 623:1255 */return this.safeKeyExpiryTime;
		/* 624: */}

	/* 625: */
	/* 626: */public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime)
	/* 627: */{
		/* 628:1265 */this.safeKeyExpiryTime = safeKeyExpiryTime;
		/* 629: */}

	/* 630: */
	/* 631: */@NotNull
	/* 632: */@Min(0L)
	/* 633: */public Integer getUploadMaxSize()
	/* 634: */{
		/* 635:1276 */return this.uploadMaxSize;
		/* 636: */}

	/* 637: */
	/* 638: */public void setUploadMaxSize(Integer uploadMaxSize)
	/* 639: */{
		/* 640:1286 */this.uploadMaxSize = uploadMaxSize;
		/* 641: */}

	/* 642: */
	/* 643: */@Length(max = 200)
	/* 644: */public String getUploadImageExtension()
	/* 645: */{
		/* 646:1296 */return this.uploadImageExtension;
		/* 647: */}

	/* 648: */
	/* 649: */public void setUploadImageExtension(String uploadImageExtension)
	/* 650: */{
		/* 651:1306 */if (uploadImageExtension != null) {
			/* 652:1307 */uploadImageExtension = uploadImageExtension
					.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
			/* 653: */}
		/* 654:1309 */this.uploadImageExtension = uploadImageExtension;
		/* 655: */}

	/* 656: */
	/* 657: */@Length(max = 200)
	/* 658: */public String getUploadFlashExtension()
	/* 659: */{
		/* 660:1319 */return this.uploadFlashExtension;
		/* 661: */}

	/* 662: */
	/* 663: */public void setUploadFlashExtension(String uploadFlashExtension)
	/* 664: */{
		/* 665:1329 */if (uploadFlashExtension != null) {
			/* 666:1330 */uploadFlashExtension = uploadFlashExtension
					.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
			/* 667: */}
		/* 668:1332 */this.uploadFlashExtension = uploadFlashExtension;
		/* 669: */}

	/* 670: */
	/* 671: */@Length(max = 200)
	/* 672: */public String getUploadMediaExtension()
	/* 673: */{
		/* 674:1342 */return this.uploadMediaExtension;
		/* 675: */}

	/* 676: */
	/* 677: */public void setUploadMediaExtension(String uploadMediaExtension)
	/* 678: */{
		/* 679:1352 */if (uploadMediaExtension != null) {
			/* 680:1353 */uploadMediaExtension = uploadMediaExtension
					.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
			/* 681: */}
		/* 682:1355 */this.uploadMediaExtension = uploadMediaExtension;
		/* 683: */}

	/* 684: */
	/* 685: */@Length(max = 200)
	/* 686: */public String getUploadFileExtension()
	/* 687: */{
		/* 688:1365 */return this.uploadFileExtension;
		/* 689: */}

	/* 690: */
	/* 691: */public void setUploadFileExtension(String uploadFileExtension)
	/* 692: */{
		/* 693:1375 */if (uploadFileExtension != null) {
			/* 694:1376 */uploadFileExtension = uploadFileExtension
					.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
			/* 695: */}
		/* 696:1378 */this.uploadFileExtension = uploadFileExtension;
		/* 697: */}

	/* 698: */
	/* 699: */@NotEmpty
	/* 700: */@Length(max = 200)
	/* 701: */public String getImageUploadPath()
	/* 702: */{
		/* 703:1389 */return this.imageUploadPath;
		/* 704: */}

	/* 705: */
	/* 706: */public void setImageUploadPath(String imageUploadPath)
	/* 707: */{
		/* 708:1399 */if (imageUploadPath != null)
		/* 709: */{
			/* 710:1400 */if (!imageUploadPath.startsWith("/")) {
				/* 711:1401 */imageUploadPath = "/" + imageUploadPath;
				/* 712: */}
			/* 713:1403 */if (!imageUploadPath.endsWith("/")) {
				/* 714:1404 */imageUploadPath = imageUploadPath + "/";
				/* 715: */}
			/* 716: */}
		/* 717:1407 */this.imageUploadPath = imageUploadPath;
		/* 718: */}

	/* 719: */
	/* 720: */@NotEmpty
	/* 721: */@Length(max = 200)
	/* 722: */public String getFlashUploadPath()
	/* 723: */{
		/* 724:1418 */return this.flashUploadPath;
		/* 725: */}

	/* 726: */
	/* 727: */public void setFlashUploadPath(String flashUploadPath)
	/* 728: */{
		/* 729:1428 */if (flashUploadPath != null)
		/* 730: */{
			/* 731:1429 */if (!flashUploadPath.startsWith("/")) {
				/* 732:1430 */flashUploadPath = "/" + flashUploadPath;
				/* 733: */}
			/* 734:1432 */if (!flashUploadPath.endsWith("/")) {
				/* 735:1433 */flashUploadPath = flashUploadPath + "/";
				/* 736: */}
			/* 737: */}
		/* 738:1436 */this.flashUploadPath = flashUploadPath;
		/* 739: */}

	/* 740: */
	/* 741: */@NotEmpty
	/* 742: */@Length(max = 200)
	/* 743: */public String getMediaUploadPath()
	/* 744: */{
		/* 745:1447 */return this.mediaUploadPath;
		/* 746: */}

	/* 747: */
	/* 748: */public void setMediaUploadPath(String mediaUploadPath)
	/* 749: */{
		/* 750:1457 */if (mediaUploadPath != null)
		/* 751: */{
			/* 752:1458 */if (!mediaUploadPath.startsWith("/")) {
				/* 753:1459 */mediaUploadPath = "/" + mediaUploadPath;
				/* 754: */}
			/* 755:1461 */if (!mediaUploadPath.endsWith("/")) {
				/* 756:1462 */mediaUploadPath = mediaUploadPath + "/";
				/* 757: */}
			/* 758: */}
		/* 759:1465 */this.mediaUploadPath = mediaUploadPath;
		/* 760: */}

	/* 761: */
	/* 762: */@NotEmpty
	/* 763: */@Length(max = 200)
	/* 764: */public String getFileUploadPath()
	/* 765: */{
		/* 766:1476 */return this.fileUploadPath;
		/* 767: */}

	/* 768: */
	/* 769: */public void setFileUploadPath(String fileUploadPath)
	/* 770: */{
		/* 771:1486 */if (fileUploadPath != null)
		/* 772: */{
			/* 773:1487 */if (!fileUploadPath.startsWith("/")) {
				/* 774:1488 */fileUploadPath = "/" + fileUploadPath;
				/* 775: */}
			/* 776:1490 */if (!fileUploadPath.endsWith("/")) {
				/* 777:1491 */fileUploadPath = fileUploadPath + "/";
				/* 778: */}
			/* 779: */}
		/* 780:1494 */this.fileUploadPath = fileUploadPath;
		/* 781: */}

	/* 782: */
	/* 783: */@NotEmpty
	/* 784: */@Email
	/* 785: */@Length(max = 200)
	/* 786: */public String getSmtpFromMail()
	/* 787: */{
		/* 788:1506 */return this.smtpFromMail;
		/* 789: */}

	/* 790: */
	/* 791: */public void setSmtpFromMail(String smtpFromMail)
	/* 792: */{
		/* 793:1516 */this.smtpFromMail = smtpFromMail;
		/* 794: */}

	/* 795: */
	/* 796: */@NotEmpty
	/* 797: */@Length(max = 200)
	/* 798: */public String getSmtpHost()
	/* 799: */{
		/* 800:1527 */return this.smtpHost;
		/* 801: */}

	/* 802: */
	/* 803: */public void setSmtpHost(String smtpHost)
	/* 804: */{
		/* 805:1537 */this.smtpHost = smtpHost;
		/* 806: */}

	/* 807: */
	/* 808: */@NotNull
	/* 809: */@Min(0L)
	/* 810: */public Integer getSmtpPort()
	/* 811: */{
		/* 812:1548 */return this.smtpPort;
		/* 813: */}

	/* 814: */
	/* 815: */public void setSmtpPort(Integer smtpPort)
	/* 816: */{
		/* 817:1558 */this.smtpPort = smtpPort;
		/* 818: */}

	/* 819: */
	/* 820: */@NotEmpty
	/* 821: */@Length(max = 200)
	/* 822: */public String getSmtpUsername()
	/* 823: */{
		/* 824:1569 */return this.smtpUsername;
		/* 825: */}

	/* 826: */
	/* 827: */public void setSmtpUsername(String smtpUsername)
	/* 828: */{
		/* 829:1579 */this.smtpUsername = smtpUsername;
		/* 830: */}

	/* 831: */
	/* 832: */@Length(max = 200)
	/* 833: */public String getSmtpPassword()
	/* 834: */{
		/* 835:1589 */return this.smtpPassword;
		/* 836: */}

	/* 837: */
	/* 838: */public void setSmtpPassword(String smtpPassword)
	/* 839: */{
		/* 840:1599 */this.smtpPassword = smtpPassword;
		/* 841: */}

	/* 842: */
	/* 843: */@NotEmpty
	/* 844: */@Length(max = 200)
	/* 845: */public String getCurrencySign()
	/* 846: */{
		/* 847:1610 */return this.currencySign;
		/* 848: */}

	/* 849: */
	/* 850: */public void setCurrencySign(String currencySign)
	/* 851: */{
		/* 852:1620 */this.currencySign = currencySign;
		/* 853: */}

	/* 854: */
	/* 855: */@NotEmpty
	/* 856: */@Length(max = 200)
	/* 857: */public String getCurrencyUnit()
	/* 858: */{
		/* 859:1631 */return this.currencyUnit;
		/* 860: */}

	/* 861: */
	/* 862: */public void setCurrencyUnit(String currencyUnit)
	/* 863: */{
		/* 864:1641 */this.currencyUnit = currencyUnit;
		/* 865: */}

	/* 866: */
	/* 867: */@NotNull
	/* 868: */@Min(0L)
	/* 869: */public Integer getStockAlertCount()
	/* 870: */{
		/* 871:1652 */return this.stockAlertCount;
		/* 872: */}

	/* 873: */
	/* 874: */public void setStockAlertCount(Integer stockAlertCount)
	/* 875: */{
		/* 876:1662 */this.stockAlertCount = stockAlertCount;
		/* 877: */}

	/* 878: */
	/* 879: */@NotNull
	/* 880: */public StockAllocationTime getStockAllocationTime()
	/* 881: */{
		/* 882:1672 */return this.stockAllocationTime;
		/* 883: */}

	/* 884: */
	/* 885: */public void setStockAllocationTime(
			StockAllocationTime stockAllocationTime)
	/* 886: */{
		/* 887:1682 */this.stockAllocationTime = stockAllocationTime;
		/* 888: */}

	/* 889: */
	/* 890: */@NotNull
	/* 891: */@Min(0L)
	/* 892: */@Digits(integer = 3, fraction = 3)
	/* 893: */public Double getDefaultPointScale()
	/* 894: */{
		/* 895:1694 */return this.defaultPointScale;
		/* 896: */}

	/* 897: */
	/* 898: */public void setDefaultPointScale(Double defaultPointScale)
	/* 899: */{
		/* 900:1704 */this.defaultPointScale = defaultPointScale;
		/* 901: */}

	/* 902: */
	/* 903: */@NotNull
	/* 904: */public Boolean getIsDevelopmentEnabled()
	/* 905: */{
		/* 906:1714 */return this.isDevelopmentEnabled;
		/* 907: */}

	/* 908: */
	/* 909: */public void setIsDevelopmentEnabled(Boolean isDevelopmentEnabled)
	/* 910: */{
		/* 911:1724 */this.isDevelopmentEnabled = isDevelopmentEnabled;
		/* 912: */}

	/* 913: */
	/* 914: */@NotNull
	/* 915: */public Boolean getIsReviewEnabled()
	/* 916: */{
		/* 917:1734 */return this.isReviewEnabled;
		/* 918: */}

	/* 919: */
	/* 920: */public void setIsReviewEnabled(Boolean isReviewEnabled)
	/* 921: */{
		/* 922:1744 */this.isReviewEnabled = isReviewEnabled;
		/* 923: */}

	/* 924: */
	/* 925: */@NotNull
	/* 926: */public Boolean getIsReviewCheck()
	/* 927: */{
		/* 928:1754 */return this.isReviewCheck;
		/* 929: */}

	/* 930: */
	/* 931: */public void setIsReviewCheck(Boolean isReviewCheck)
	/* 932: */{
		/* 933:1764 */this.isReviewCheck = isReviewCheck;
		/* 934: */}

	/* 935: */
	/* 936: */@NotNull
	/* 937: */public ReviewAuthority getReviewAuthority()
	/* 938: */{
		/* 939:1774 */return this.reviewAuthority;
		/* 940: */}

	/* 941: */
	/* 942: */public void setReviewAuthority(ReviewAuthority reviewAuthority)
	/* 943: */{
		/* 944:1784 */this.reviewAuthority = reviewAuthority;
		/* 945: */}

	/* 946: */
	/* 947: */@NotNull
	/* 948: */public Boolean getIsConsultationEnabled()
	/* 949: */{
		/* 950:1794 */return this.isConsultationEnabled;
		/* 951: */}

	/* 952: */
	/* 953: */public void setIsConsultationEnabled(Boolean isConsultationEnabled)
	/* 954: */{
		/* 955:1804 */this.isConsultationEnabled = isConsultationEnabled;
		/* 956: */}

	/* 957: */
	/* 958: */@NotNull
	/* 959: */public Boolean getIsConsultationCheck()
	/* 960: */{
		/* 961:1814 */return this.isConsultationCheck;
		/* 962: */}

	/* 963: */
	/* 964: */public void setIsConsultationCheck(Boolean isConsultationCheck)
	/* 965: */{
		/* 966:1824 */this.isConsultationCheck = isConsultationCheck;
		/* 967: */}

	/* 968: */
	/* 969: */@NotNull
	/* 970: */public ConsultationAuthority getConsultationAuthority()
	/* 971: */{
		/* 972:1834 */return this.consultationAuthority;
		/* 973: */}

	/* 974: */
	/* 975: */public void setConsultationAuthority(
			ConsultationAuthority consultationAuthority)
	/* 976: */{
		/* 977:1844 */this.consultationAuthority = consultationAuthority;
		/* 978: */}

	/* 979: */
	/* 980: */@NotNull
	/* 981: */public Boolean getIsInvoiceEnabled()
	/* 982: */{
		/* 983:1854 */return this.isInvoiceEnabled;
		/* 984: */}

	/* 985: */
	/* 986: */public void setIsInvoiceEnabled(Boolean isInvoiceEnabled)
	/* 987: */{
		/* 988:1864 */this.isInvoiceEnabled = isInvoiceEnabled;
		/* 989: */}

	/* 990: */
	/* 991: */@NotNull
	/* 992: */public Boolean getIsTaxPriceEnabled()
	/* 993: */{
		/* 994:1874 */return this.isTaxPriceEnabled;
		/* 995: */}

	/* 996: */
	/* 997: */public void setIsTaxPriceEnabled(Boolean isTaxPriceEnabled)
	/* 998: */{
		/* 999:1884 */this.isTaxPriceEnabled = isTaxPriceEnabled;
		/* 1000: */}

	/* 1001: */
	/* 1002: */@NotNull
	/* 1003: */@Min(0L)
	/* 1004: */@Digits(integer = 3, fraction = 3)
	/* 1005: */public Double getTaxRate()
	/* 1006: */{
		/* 1007:1896 */return this.taxRate;
		/* 1008: */}

	/* 1009: */
	/* 1010: */public void setTaxRate(Double taxRate)
	/* 1011: */{
		/* 1012:1906 */this.taxRate = taxRate;
		/* 1013: */}

	/* 1014: */
	/* 1015: */@NotEmpty
	/* 1016: */@Length(max = 200)
	/* 1017: */public String getCookiePath()
	/* 1018: */{
		/* 1019:1917 */return this.cookiePath;
		/* 1020: */}

	/* 1021: */
	/* 1022: */public void setCookiePath(String cookiePath)
	/* 1023: */{
		/* 1024:1927 */if ((cookiePath != null) && (!cookiePath.endsWith("/"))) {
			/* 1025:1928 */cookiePath = cookiePath + "/";
			/* 1026: */}
		/* 1027:1930 */this.cookiePath = cookiePath;
		/* 1028: */}

	/* 1029: */
	/* 1030: */@Length(max = 200)
	/* 1031: */public String getCookieDomain()
	/* 1032: */{
		/* 1033:1940 */return this.cookieDomain;
		/* 1034: */}

	/* 1035: */
	/* 1036: */public void setCookieDomain(String cookieDomain)
	/* 1037: */{
		/* 1038:1950 */this.cookieDomain = cookieDomain;
		/* 1039: */}

	/* 1040: */
	/* 1041: */@Length(max = 200)
	/* 1042: */public String getKuaidi100Key()
	/* 1043: */{
		/* 1044:1960 */return this.kuaidi100Key;
		/* 1045: */}

	/* 1046: */
	/* 1047: */public void setKuaidi100Key(String kuaidi100Key)
	/* 1048: */{
		/* 1049:1970 */this.kuaidi100Key = kuaidi100Key;
		/* 1050: */}

	/* 1051: */
	/* 1052: */public Boolean getIsCnzzEnabled()
	/* 1053: */{
		/* 1054:1979 */return this.isCnzzEnabled;
		/* 1055: */}

	/* 1056: */
	/* 1057: */public void setIsCnzzEnabled(Boolean isCnzzEnabled)
	/* 1058: */{
		/* 1059:1989 */this.isCnzzEnabled = isCnzzEnabled;
		/* 1060: */}

	/* 1061: */
	/* 1062: */public String getCnzzSiteId()
	/* 1063: */{
		/* 1064:1998 */return this.cnzzSiteId;
		/* 1065: */}

	/* 1066: */
	/* 1067: */public void setCnzzSiteId(String cnzzSiteId)
	/* 1068: */{
		/* 1069:2008 */this.cnzzSiteId = cnzzSiteId;
		/* 1070: */}

	/* 1071: */
	/* 1072: */public String getCnzzPassword()
	/* 1073: */{
		/* 1074:2017 */return this.cnzzPassword;
		/* 1075: */}

	/* 1076: */
	/* 1077: */public void setCnzzPassword(String cnzzPassword)
	/* 1078: */{
		/* 1079:2027 */this.cnzzPassword = cnzzPassword;
		/* 1080: */}

	/* 1081: */
	/* 1082: */public String[] getHotSearches()
	/* 1083: */{
		/* 1084:2036 */return StringUtils.split(this.hotSearch, ",");
		/* 1085: */}

	/* 1086: */
	/* 1087: */public String[] getDisabledUsernames()
	/* 1088: */{
		/* 1089:2045 */return StringUtils.split(this.disabledUsername, ",");
		/* 1090: */}

	/* 1091: */
	/* 1092: */public String[] getUploadImageExtensions()
	/* 1093: */{
		/* 1094:2054 */return StringUtils.split(this.uploadImageExtension, ",");
		/* 1095: */}

	/* 1096: */
	/* 1097: */public String[] getUploadFlashExtensions()
	/* 1098: */{
		/* 1099:2063 */return StringUtils.split(this.uploadFlashExtension, ",");
		/* 1100: */}

	/* 1101: */
	/* 1102: */public String[] getUploadMediaExtensions()
	/* 1103: */{
		/* 1104:2072 */return StringUtils.split(this.uploadMediaExtension, ",");
		/* 1105: */}

	/* 1106: */
	/* 1107: */public String[] getUploadFileExtensions()
	/* 1108: */{
		/* 1109:2081 */return StringUtils.split(this.uploadFileExtension, ",");
		/* 1110: */}

	/* 1111: */
	public BigDecimal setScale(BigDecimal amount)
	/* 1113: */{
		/* 1114:2092 */if (amount == null) {
			/* 1115:2093 */return null;
			/* 1116: */}
		/* 1117: */int roundingMode;
		/* 1118: */// int roundingMode;
		/* 1119:2096 */if (getPriceRoundType() == RoundType.roundUp)
		/* 1120: */{
			/* 1121:2097 */roundingMode = 0;
			/* 1122: */}
		/* 1123: */else
		/* 1124: */{
			/* 1125: */// int roundingMode;
			/* 1126:2098 */if (getPriceRoundType() == RoundType.roundDown) {
				/* 1127:2099 */roundingMode = 1;
				/* 1128: */} else {
				/* 1129:2101 */roundingMode = 4;
				/* 1130: */}
			/* 1131: */}
		/* 1132:2103 */return amount.setScale(getPriceScale().intValue(),
				roundingMode);
		/* 1133: */}
	/* 1134: */
}

/*
 * Location: D:\workspace\shopxx\WEB-INF\classes\
 * 
 * Qualified Name: net.shopxx.Setting
 * 
 * JD-Core Version: 0.7.0.1
 */