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
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
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
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public class BufferedFile extends File {
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1, openToSubPlugin = false))
	private static final long serialVersionUID = 1L;
	/**
	 * 存在缓冲中的文件各行。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1, openToSubPlugin = false))
	private final List<Object> lines = new ArrayList<Object>();
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public BufferedFile(File parend, String child) {
		super(parend, child);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public BufferedFile(String pathname) {
		super(pathname);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public BufferedFile(String parent, String child) {
		super(parent, child);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public BufferedFile(URI uri) {
		super(uri);
	}
	/**
	 * 根据给定的文件创建一个{@code BufferedFile}对象。
	 * 
	 * @param file
	 *            给定的文件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public BufferedFile(File file) {
		super(file.getPath());
	}
	/**
	 * 获取缓存区内的内容，
	 * 以每行的字符串作为列表。
	 * 
	 * @return 缓存区内容。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public List<Object> lines() {
		return lines;
	}
	/**
	 * 将文件中的内容读入缓存区。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	/**
	 * 在产生这个文件前，确保所有上级文件夹已经产生。
	 * 如果没有产生，则先产生上级文件夹。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@Override
	public boolean createNewFile() throws IOException {
		File parent = getParentFile();
		parent.mkdirs();
		return super.createNewFile();
	}
}
