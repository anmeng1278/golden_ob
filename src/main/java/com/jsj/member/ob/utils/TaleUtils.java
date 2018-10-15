package com.jsj.member.ob.utils;

import com.jsj.member.ob.exception.TipException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaleUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaleUtils.class);
	/**
	 * 一个月
	 */
	@SuppressWarnings("unused")
	private static final int one_month = 30 * 24 * 60 * 60;
	/**
	 * 匹配邮箱正则
	 */
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);
	// 使用双重检查锁的单例方式需要添加 volatile 关键字
	private static volatile DataSource newDataSource;

	/**
	 * 获取文件所在目录
	 */
	@SuppressWarnings("unused")
	private static String location = TaleUtils.class.getClassLoader().getResource("").getPath();

	/**
	 * 判断是否是邮箱
	 *
	 * @param emailStr
	 * @return
	 */
	public static boolean isEmail(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	/**
	 * @param fileName
	 *            获取jar外部的文件
	 * @return 返回属性
	 */
	private static Properties getPropFromFile(String fileName) {
		Properties properties = new Properties();
		try {
			// 默认是classPath路径
			InputStream resourceAsStream = new FileInputStream(fileName);
			properties.load(resourceAsStream);
		} catch (TipException | IOException e) {
			LOGGER.error("get properties file fail={}", e.getMessage());
		}
		return properties;
	}

	/**
	 * md5加密
	 *
	 * @param source
	 *            数据源
	 * @return 加密字符串
	 */
	public static String MD5encode(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ignored) {
		}
		byte[] encode = messageDigest.digest(source.getBytes());
		StringBuilder hexString = new StringBuilder();
		for (byte anEncode : encode) {
			String hex = Integer.toHexString(0xff & anEncode);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * 获取新的数据源
	 *
	 * @return
	 */
	public static DataSource getNewDataSource() {
		if (newDataSource == null)
			synchronized (TaleUtils.class) {
				if (newDataSource == null) {
					Properties properties = TaleUtils.getPropFromFile("application-default.properties");
					if (properties.size() == 0) {
						return newDataSource;
					}
					DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
					managerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
					managerDataSource.setPassword(properties.getProperty("spring.datasource.password"));
					String str = "jdbc:mysql://" + properties.getProperty("spring.datasource.url") + "/"
							+ properties.getProperty("spring.datasource.dbname")
							+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
					managerDataSource.setUrl(str);
					managerDataSource.setUsername(properties.getProperty("spring.datasource.username"));
					newDataSource = managerDataSource;
				}
			}
		return newDataSource;
	}


	/**
	 * 从cookies中获取指定cookie
	 *
	 * @param name
	 *            名称
	 * @param request
	 *            请求
	 * @return cookie
	 */
	private static Cookie cookieRaw(String name, HttpServletRequest request) {
		Cookie[] servletCookies = request.getCookies();
		if (servletCookies == null) {
			return null;
		}
		for (Cookie c : servletCookies) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}


	/**
	 * 过滤XSS注入
	 *
	 * @param value
	 * @return
	 */
	public static String filterXSS(String value) {
		String cleanValue = null;
		if (value != null) {
			cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);
			// Avoid null characters
			cleanValue = cleanValue.replaceAll("\0", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
		}
		return cleanValue;
	}
	
	public static String getFileKey(String name) {
        String prefix = "/upload/" + DateUtils.dateFormat(new Date(), "yyyy/MM");
        if (!new File(TaleUtils.getUplodFilePath() + prefix).exists()) {
            new File(TaleUtils.getUplodFilePath() + prefix).mkdirs();
        }

        name = StringUtils.trimToNull(name);
        if (name == null) {
            return prefix + "/" + com.jsj.member.ob.utils.StringUtils.UUID32() + "." + null;
        } else {
            name = name.replace('\\', '/');
            name = name.substring(name.lastIndexOf("/") + 1);
            int index = name.lastIndexOf(".");
            String ext = null;
            if (index >= 0) {
                ext = StringUtils.trimToNull(name.substring(index + 1));
            }

            return prefix + "/" + com.jsj.member.ob.utils.StringUtils.UUID32() + "." + (ext == null ? null : (ext));
        }
    }

	/**
	 * 判断是否是合法路径
	 *
	 * @param slug
	 * @return
	 */
	public static boolean isPath(String slug) {
		if (StringUtils.isNotBlank(slug)) {
			if (slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
				return false;
			}
			Matcher matcher = SLUG_REGEX.matcher(slug);
			return matcher.find();
		}
		return false;
	}

	/**
	 * 判断文件是否是图片类型
	 *
	 * @param imageFile
	 * @return
	 */
	public static boolean isImage(InputStream imageFile) {
		try {
			Image img = ImageIO.read(imageFile);
			if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 随机数
	 *
	 * @param size
	 * @return
	 */
	public static String getRandomNumber(int size) {
		String num = "";

		for (int i = 0; i < size; ++i) {
			double a = Math.random() * 9.0D;
			a = Math.ceil(a);
			int randomNum = (new Double(a)).intValue();
			num = num + randomNum;
		}

		return num;
	}

	/**
	 * 获取保存文件的位置,jar所在目录的路径
	 *
	 * @return
	 */
	public static String getUplodFilePath() {
		String path = TaleUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.length());
		try {
			path = java.net.URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int lastIndex = path.lastIndexOf("/") + 1;
		path = path.substring(0, lastIndex);
		File file = new File("");
		return file.getAbsolutePath() + "/src/main/webapp/";
	}

}
