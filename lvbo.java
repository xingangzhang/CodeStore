import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Timestamp;

public class lvbo {
	static ArrayList<Float> Data_Output = new ArrayList<Float>();
	static ArrayList<Float> Data_Output1 = new ArrayList<Float>();
	static ArrayList<Float> Data_Output2 = new ArrayList<Float>();
	static ArrayList<Float> datax_average = new ArrayList<Float>();
	static float bz[] = { 0.0f, -0.0f, 0.0f, -0.0f, 0.0f }; // 分子
	static float az[] = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f }; // 分母
//	static float bz[] = { 0.0117f, -0.0329f, 0.0451f, -0.0329f, 0.0117f }; // 分子
//	static float az[] = { 1.0000f, -3.6048f, 4.9787f, -3.1156f, 0.7447f }; // 分母
	int frequency = 1;
	static int filterBegin = 5;
	static float xBuf[] = new float[5];
	static float yBuf[] = new float[5];
	static float xBuf1[] = new float[5];
	static float yBuf1[] = new float[5];
	static float xBuf2[] = new float[5];
	static float yBuf2[] = new float[5];
	float num = 0;
	float num1 = 0;
	float num2 = 0;

	public void readfromfile(ArrayList<Float> datax, ArrayList<Float> datay, ArrayList<Float> dataz)
			throws IOException {
		InputStream is = null;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		int i = 1;
		int j;
		try {
			// create file input stream
			is = new FileInputStream("1.txt");
			// create new data input stream
			dis = new DataInputStream(is);
			// read till end of the stream
			while (dis.available() > 0) {
				// read character
				for (j = 0; j < (frequency) * 3; j++) {
					if (dis.available() > 0) {
						switch (i) {
						case 1:
							num = num + dis.readFloat();
							i++;
							break;
						case 2:
							num1 = num1 + dis.readFloat();
							i++;
							break;
						case 3:
							num2 = num2 + dis.readFloat();
							i = 1;
							break;
						}
					} else {
						break;
					}

				}
				if (j == (frequency * 3)) {
					datax.add(num / frequency);
					num = 0;
					datay.add(num1 / frequency);
					num1 = 0;
					dataz.add(num2 / frequency);
					num2 = 0;
				}
				/*
				 * switch(i){ case 0: datax.add(dis.readFloat()); i++; break;
				 * 
				 * case 1: datay.add(dis.readFloat()); i++; break; case 2:
				 * dataz.add(dis.readFloat()); i=0; break; }
				 */
			}
		} catch (Exception e) {
			// if any I/O error occurs
			e.printStackTrace();
		} finally {
			// releases all system resources from the streams
			if (is != null)
				is.close();
			if (dos != null)
				is.close();
			if (dis != null)
				dis.close();
			if (fos != null)
				fos.close();
		}
	}
	
	public void readFile(ArrayList<Float> datax, ArrayList<Float> datay, ArrayList<Float> dataz)
			throws IOException {
		File file = null;
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			file = new File("161.txt");
			if (file.isFile() && file.exists()) {
				read = new InputStreamReader(new FileInputStream(file));
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] data = lineTxt.split(",");
					datax.add(Float.valueOf(data[0]));
					datay.add(Float.valueOf(data[1]));
					dataz.add(Float.valueOf(data[2]));
				}
			}

		} catch (Exception e) {
			// if any I/O error occurs
			e.printStackTrace();
		} finally {
			// releases all system resources from the streams
			bufferedReader.close();
			read.close();
		}
	}
	
	public static void writeFile(ArrayList<Float> datax, ArrayList<Float> datay, ArrayList<Float> dataz) throws IOException {
		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;
		try {
			writer = new FileWriter("162.txt");
			bufferedWriter = new BufferedWriter(writer);
			for (int i = 0; i < datax.size(); i++) {
				String data = datax.get(i).toString() + ',' + datay.get(i).toString() + ',' + dataz.get(i).toString() + '\n';
				bufferedWriter.write(data);
			}

		} catch (Exception e) {
			// if any I/O error occurs
			e.printStackTrace();
		} finally {
			// releases all system resources from the streams
			bufferedWriter.close();
			writer.close();
		}
	}

	// float Data_Output[DATA_LENTH]; //输出数据
	static void but_filter(ArrayList<Float> datax, ArrayList<Float> datay, ArrayList<Float> dataz) {
		for (int j = 0; j < datax.size(); j++) {
			for (int i = 4; i > 0; i--) {
				yBuf[i] = yBuf[i - 1];
				xBuf[i] = xBuf[i - 1];
				yBuf1[i] = yBuf1[i - 1];
				xBuf1[i] = xBuf1[i - 1];
				yBuf2[i] = yBuf2[i - 1];
				xBuf2[i] = xBuf2[i - 1];
			}
			xBuf[0] = datax.get(j);
			xBuf1[0] = datay.get(j);
			xBuf2[0] = dataz.get(j);
			if (filterBegin > 0) {
				filterBegin = 0;
				yBuf[0] = datax.get(j);
				yBuf1[0] = datay.get(j);
				yBuf2[0] = dataz.get(j);

			} else {
				yBuf[0] = bz[0] * (xBuf[0] + xBuf[4]) + bz[1] * (xBuf[1] + xBuf[3]) + bz[2] * xBuf[2]
						- (az[1] * yBuf[1] + az[2] * yBuf[2] + az[3] * yBuf[3] + az[4] * yBuf[4]);
				yBuf1[0] = bz[0] * (xBuf1[0] + xBuf1[4]) + bz[1] * (xBuf1[1] + xBuf1[3]) + bz[2] * xBuf1[2]
						- (az[1] * yBuf1[1] + az[2] * yBuf1[2] + az[3] * yBuf1[3] + az[4] * yBuf1[4]);
				yBuf2[0] = bz[0] * (xBuf2[0] + xBuf2[4]) + bz[1] * (xBuf2[1] + xBuf2[3]) + bz[2] * xBuf2[2]
						- (az[1] * yBuf2[1] + az[2] * yBuf2[2] + az[3] * yBuf2[3] + az[4] * yBuf2[4]);

			}
			Data_Output.add(yBuf[0]);
			Data_Output1.add(yBuf1[0]);
			Data_Output2.add(yBuf2[0]);
		}
	}

	public static void writedata(ArrayList<Float> datax, ArrayList<Float> datay, ArrayList<Float> dataz)
			throws IOException {
		InputStream is = null;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try {
			// create file output stream
			fos = new FileOutputStream("lvbo.txt");

			// create data output stream
			dos = new DataOutputStream(fos);
			for (int i = 0; i < datax.size(); i++) {
				dos.writeFloat(datax.get(i));
				dos.writeFloat(datay.get(i));
				dos.writeFloat(dataz.get(i));

			}
			// force bytes to the underlying stream
			dos.flush();
		} catch (Exception e) {
			// if any I/O error occurs
			e.printStackTrace();
		} finally {
			// releases all system resources from the streams
			if (is != null)
				is.close();
			if (dos != null)
				dos.close();
			if (dis != null)
				dis.close();
			if (fos != null)
				fos.close();
		}
	}

	public static void main(String[] args) throws IOException {
		ArrayList<Float> datax = new ArrayList<Float>();
		ArrayList<Float> datay = new ArrayList<Float>();
		ArrayList<Float> dataz = new ArrayList<Float>();
		lvbo readf = new lvbo();
//		readf.readfromfile(datax, datay, dataz);
		readf.readFile(datax, datay, dataz);
		but_filter(datax, datay, dataz);
		writeFile(Data_Output, Data_Output1, Data_Output2);
	}
}