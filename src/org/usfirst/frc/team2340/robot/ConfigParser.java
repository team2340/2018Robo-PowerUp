package org.usfirst.frc.team2340.robot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigParser {
	static private Map<String, String> map = new HashMap<String, String>();

	public ConfigParser(String filePath) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String myLine = null;
			while ((myLine = in.readLine()) != null)
			{
				String[] split = myLine.split(" = ");
				if(split.length == 2) {
					map.put(split[0], split[1]);
				}
				else {
					System.out.println("Could not interpret: "+myLine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				for(String val : map.keySet()) {
					System.out.println(val+" | "+map.get(val));
				}
			}catch (Exception e) {}
		}
	}
	
	@SuppressWarnings("unchecked")
	// XXX Future concept note:
	// Could make it return Object and have implicit cast on return instead of
	// forcing info to be the same type as the return type (want Double, store as
	// Integer)
	public static <T> T get(String key, Class<T> info) {
		String val = map.get(key);
		Object obj = null;
		if(val != null) {
			try {
				if(info == Boolean.class) {
					obj = Boolean.valueOf(val);
				}
				else if(info == Double.class) {
					obj = Double.valueOf(val);
				}
				else if(info == Integer.class) {
					obj = Integer.valueOf(val);
				}
				else if(info == Float.class) {
					obj = Float.valueOf(val);
				}
				else {
					System.out.println("Unsupported type: " + info + " for " + val);
				}
			} catch (Exception e) {
				System.out.println("Could not convert " + val + " to type " + info);
			}
		}
		else {
			System.out.println("Could not find value for: " + key);
		}
		return (T) obj;
	}
}
