/**
 * 
 */
package org.msh.etbm.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.msh.etbm.entities.WeeklyFrequency;

import com.rmemoria.datastream.DataConverter;
import com.rmemoria.datastream.DataUnmarshaller;
import com.rmemoria.datastream.ObjectConsumer;
import com.rmemoria.datastream.StreamContext;
import com.rmemoria.datastream.StreamContextFactory;
import com.rmemoria.datastream.StreamFileTypeXML;

/**
 * @author Ricardo Memoria
 *
 */
public class PackageTest {

	public static void main(String[] args) throws Exception {
		PackageTest pkg = new PackageTest();
		pkg.run();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	private void run() throws Exception {
		URL schema = getClass().getClassLoader().getResource("datastream/clientinifile-schema.xml");
		StreamContext context = StreamContextFactory.createContext(schema);

		context.setConverter(WeeklyFrequency.class, new DataConverter() {
			@Override
			public String convertToString(Object obj) {
				return Integer.toString( ((WeeklyFrequency)obj).getValue() );
			}
			
			@Override
			public Object convertFromString(String s, Class clazz) {
				WeeklyFrequency wf = new WeeklyFrequency();
				wf.setValue( Integer.parseInt(s) );
				return wf;
			}
		});

		DataUnmarshaller um = context.createUnmarshaller(StreamFileTypeXML.class);

		long ticks = System.currentTimeMillis();
		File file = uncompressFile(new File("C:\\Projetos\\etbmanager\\desktop\\HEALTH_CENTER_1.etbm.pkg"));
		System.out.println(System.currentTimeMillis() - ticks);
		

		ticks = System.currentTimeMillis();
		FileInputStream fin = new FileInputStream(file);
//		GZIPInputStream zin = new GZIPInputStream(fin);
		um.unmarshall(fin, new ObjectConsumer() {
			
			@Override
			public void startObjectReading(Class objectClass) {
				//
			}
			
			@Override
			public void onNewObject(Object object) {
				//
			}
		});
//		zin.close();
		fin.close();
		System.out.println(System.currentTimeMillis() - ticks);
		
	}


	private File uncompressFile(File gzipfile) {
		try {
			byte[] buffer = new byte[65536];

			String s = gzipfile.getAbsolutePath();
			s = s.substring(0, s.length() - 4);
			File filedest = new File(s);
			GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(gzipfile));
			FileOutputStream out = new FileOutputStream(filedest);

			int noRead;
			while ((noRead = gzin.read(buffer)) != -1) {
			        out.write(buffer, 0, noRead);
			}
			gzin.close();
			out.close();
			
			return filedest;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
