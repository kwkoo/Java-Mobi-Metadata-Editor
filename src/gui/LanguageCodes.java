package gui;

public class LanguageCodes
{
	// these codes are taken from:
	// http://www.autoitscript.com/autoit3/docs/appendix/OSLangCodes.htm
	//
	public final static int[] code =
	{ 0, 1078, 1052, 1025, 2049, 3073, 4097, 5121, 6145, 7169, 8193, 9217,
			10241, 11265, 12289, 13313, 14337, 15361, 16385, 1067, 1068, 2092,
			1069, 1059, 1026, 1027, 4, 1028, 2052, 3076, 4100, 5124, 1050, 1029,
			1030, 19, 1043, 2067, 9, 1033, 2057, 3081, 4105, 5129, 6153, 7177,
			8201, 9225, 10249, 11273, 12297, 13321, 1061, 1080, 1073, 1035, 12,
			1036, 2060, 3084, 4108, 5132, 6156, 1079, 7, 1031, 2055, 3079, 4103,
			5127, 1032, 1037, 1081, 1038, 1039, 1057, 16, 1040, 2064, 1041, 1087,
			1111, 1042, 1062, 1063, 1071, 1086, 2110, 1102, 1044, 2068, 1045, 22,
			1046, 2070, 1048, 1049, 1103, 2074, 3098, 1051, 1060, 10, 1034, 2058,
			3082, 4106, 5130, 6154, 7178, 8202, 9226, 10250, 11274, 12298,
			13322, 14346, 15370, 16394, 17418, 18442, 19466, 20490, 1089, 29, 1053,
			2077, 1097, 1092, 1054, 1055, 1058, 1056, 1091, 2115, 1066 };

	public final static String[] description =
	{ "Unspecified", "Afrikaans", "Albanian", "Arabic - Saudi Arabia",
			"Arabic - Iraq", "Arabic - Egypt", "Arabic - Libya",
			"Arabic - Algeria", "Arabic - Morocco", "Arabic - Tunisia",
			"Arabic - Oman", "Arabic - Yemen", "Arabic - Syria",
			"Arabic - Jordan", "Arabic - Lebanon", "Arabic - Kuwait",
			"Arabic - UAE", "Arabic - Bahrain", "Arabic - Qatar", "Armenian",
			"Azeri - Latin", "Azeri - Cyrillic", "Basque", "Belarusian",
			"Bulgarian", "Catalan", "Chinese", "Chinese - Taiwan", "Chinese - PRC",
			"Chinese - Hong Kong", "Chinese - Singapore", "Chinese - Macau",
			"Croatian", "Czech", "Danish", "Dutch", "Dutch - Standard",
			"Dutch - Belgian", "English", "English - United States",
			"English Ð United Kingdom", "English - Australian",
			"English - Canadian", "English - New Zealand", "English - Irish",
			"English - South Africa", "English - Jamaica",
			"English - Caribbean", "English - Belize", "English - Trinidad",
			"English - Zimbabwe", "English - Philippines", "Estonian",
			"Faeroese", "Farsi", "Finnish", "French", "French - Standard",
			"French - Belgian", "French - Canadian", "French - Swiss",
			"French - Luxembourg", "French - Monaco", "Georgian", "German",
			"German - Standard", "German - Swiss", "German - Austrian",
			"German - Luxembourg", "German - Liechtenstei", "Greek", "Hebrew",
			"Hindi", "Hungarian", "Icelandic", "Indonesian", "Italian",
			"Italian - Standard", "Italian - Swiss", "Japanese", "Kazakh",
			"Konkani", "Korean", "Latvian", "Lithuanian", "Macedonian",
			"Malay - Malaysia", "Malay - Brunei Darussalam", "Marathi",
			"Norwegian - Bokmal", "Norwegian - Nynorsk", "Polish", "Portugese",
			"Portuguese - Brazilian", "Portuguese - Standard", "Romanian",
			"Russian", "Sanskrit", "Serbian - Latin", "Serbian - Cyrillic",
			"Slovak", "Slovenian", "Spanish", "Spanish - Traditional Sort",
			"Spanish - Mexican", "Spanish - Modern Sort",
			"Spanish - Guatemala", "Spanish - Costa Rica", "Spanish - Panama",
			"Spanish - Dominican Republic", "Spanish - Venezuela",
			"Spanish - Colombia", "Spanish - Peru", "Spanish - Argentina",
			"Spanish - Ecuador", "Spanish - Chile", "Spanish - Uruguay",
			"Spanish - Paraguay", "Spanish - Bolivia", "Spanish - El Salvador",
			"Spanish - Honduras", "Spanish - Nicaragua",
			"Spanish - Puerto Rico", "Swahili", "Swedish", "Swedish - Finland",
			"Tamil", "Tatar", "Thai", "Turkish", "Ukrainian", "Urdu",
			"Uzbek - Latin", "Uzbek - Cyrillic", "Vietnamese" };


	public static int codeToIndex(int key)
	{
		int len = code.length;
		for (int i = 0; i < len; i++)
		{
			if (code[i] == key) return i;
		}

		return -1;
	}
}
