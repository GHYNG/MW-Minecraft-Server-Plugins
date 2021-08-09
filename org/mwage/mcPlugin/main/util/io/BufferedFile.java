package org.mwage.mcPlugin.main.util.io;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
/**
 * 有缓存区的文件。
 * <p>
 * 在处理文件时，先将文件内容统一存入缓存区。
 * 可以对缓存区进行各种修改。
 * 修改完毕后，再统一将缓存区写入文件。
 * 缓存区是一个字符串的列表，
 * 作为不同的行。
 * 
 * @author GHYNG
 */
public class BufferedFile extends File {
	private static final long serialVersionUID = 1L;
	private final List<Object> lines = new ArrayList<Object>();
	public BufferedFile(File parend, String child) {
		super(parend, child);
	}
	public BufferedFile(String pathname) {
		super(pathname);
	}
	public BufferedFile(String parent, String child) {
		super(parent, child);
	}
	public BufferedFile(URI uri) {
		super(uri);
	}
	public BufferedFile(File file) {
		super(file.getPath());
	}
	/**
	 * 获取缓存区内的内容，
	 * 以每行的字符串作为列表。
	 * 
	 * @return 缓存区内容。
	 */
	public List<Object> getLines() {
		return lines;
	}
	/**
	 * 将文件中的内容读入缓存区。
	 */
	public void read() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this));
			lines.clear();
			String line = reader.readLine();
			while(line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将缓存区中的内容写入文件。
	 */
	public void write() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this));
			for(Object line : lines) {
				writer.write(line.toString());
				writer.newLine();
			}
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
