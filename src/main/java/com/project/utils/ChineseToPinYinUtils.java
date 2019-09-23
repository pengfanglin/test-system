package com.project.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转换为拼音
 */
public class ChineseToPinYinUtils {
  /**
   * 获取字符串内的所有汉字的汉语拼音并大写每个字的首字母
   */
  public static String firstCharUpper(String chinese) {
    if (chinese == null) {
      return null;
    }
    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不标声调
    format.setVCharType(HanyuPinyinVCharType.WITH_V);// u:的声母替换为v
    try {
      StringBuilder sb = new StringBuilder();
      int i = 0;
      while (i < chinese.length()) {
        String[] array = PinyinHelper.toHanyuPinyinStringArray(chinese
          .charAt(i), format);
        if (array == null || array.length == 0) {
          i++;
          continue;
        }
        String s = array[0];// 不管多音字,只取第一个
        char c = s.charAt(0);// 大写第一个字母
        String pinyin = String.valueOf(c).toUpperCase().concat(s
          .substring(1));
        sb.append(pinyin);
        i++;
      }
      return sb.toString();
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取字符串拼音的第一个字母
   */
  public static String toFirstChar(String chinese) {
    StringBuilder pinyinStr = new StringBuilder();
    char[] newChar = chinese.toCharArray();  //转为单个字符
    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    for (char aNewChar : newChar) {
      if (aNewChar > 128) {
        try {
          pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(aNewChar, defaultFormat)[0].charAt(0));
        } catch (BadHanyuPinyinOutputFormatCombination e) {
          e.printStackTrace();
        }
      } else {
        pinyinStr.append(aNewChar);
      }
    }
    return pinyinStr.toString();
  }

  /**
   * 汉字转为拼音
   */
  public static String toPinyin(String chinese) {
    StringBuilder pinyinStr = new StringBuilder();
    char[] newChar = chinese.toCharArray();
    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    for (char aNewChar : newChar) {
      if (aNewChar > 128) {
        try {
          pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(aNewChar, defaultFormat)[0]);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
          e.printStackTrace();
        }
      } else {
        pinyinStr.append(aNewChar);
      }
    }
    return pinyinStr.toString();
  }
}