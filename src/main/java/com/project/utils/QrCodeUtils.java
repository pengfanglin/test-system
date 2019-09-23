package com.project.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码
 */
public class QrCodeUtils {
	/**
	 * 生成二维码
	 */
	public static void createQrCode(String filePath, String content) {
		//文件保存根目录
		String rootPath = OthersUtils.getFileSaveParentPath();
		// 文件完整路径
		filePath = rootPath + "/" + filePath;
		//文件夹路径
		File dirPath = new File(filePath.substring(0, filePath.lastIndexOf("/")));
		if (!dirPath.exists()) {
			boolean success = dirPath.mkdirs();
			if (!success) {
				throw new RuntimeException("创建目录失败");
			}
		}
		int width = 200;
		int height = 200;
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 生成矩阵
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			Path path = FileSystems.getDefault().getPath(filePath);
			MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成图片失败");
		}
	}

	/**
	 * 解析二维码数据
	 */
	public static String readQrCode(String filePath) {
		//文件保存根目录
		String rootPath = OthersUtils.getFileSaveParentPath();
		BufferedImage image;
		try {
			image = ImageIO.read(new File(rootPath + filePath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			// 对图像进行解码
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("二维码解析失败");
		}
	}
}
