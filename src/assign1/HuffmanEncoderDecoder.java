package assign1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Assignment 1 Submitted by: Javier Giberg. ID# 302280383 Netanel Bitton. ID#
 * 305484651
 */

final public class HuffmanEncoderDecoder implements Compressor {

	private static HashMap<Byte, String> Compress;
	private static HashMap<Byte, String> root;
	private static StringBuilder stringBuilder;

	public HuffmanEncoderDecoder() {
		this.Compress = new HashMap<>();
		this.root = new HashMap<>();
		this.stringBuilder = new StringBuilder();
	}

//-------------------------------------------------Compress------------------------------------------------------
	public void Compress(String[] input_names, String[] output_names) {
		try {
			FileInputStream fileinputstream = new FileInputStream(input_names[0]);
			byte[] fileBytes = new byte[fileinputstream.available()];
			fileinputstream.read(fileBytes);
			byte[] Reault = BuildFile(fileBytes);
			OutputStream outputstream = new FileOutputStream(output_names[0]);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
			objectoutputstream.writeObject(Reault);
			objectoutputstream.writeObject(Compress);
			objectoutputstream.close();
			outputstream.close();
			fileinputstream.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

//---------------------------------------------------------------------------------------------------------------
	static byte[] BuildFile(byte[] bitFile) {
		List<Node> list = getList(bitFile);
		Node root = createTree(list);
		root.Order();
		Compress = getRoot(root);
		return convertStringToByte(bitFile, Compress);
	}

//---------------------------------------------------------------------------------------------------------------
	static List<Node> getList(byte[] contentbytes) {
		ArrayList<Node> Return = new ArrayList<Node>();
		HashMap<Byte, Integer> map = new HashMap<>();

		for (int i = 0; i < contentbytes.length; i++) {
			Integer count = map.get(contentbytes[i]);
			if (count == null) {
				map.put(contentbytes[i], 1);
			} else {
				map.put(contentbytes[i], count + 1);
			}
		}

		for (Map.Entry<Byte, Integer> temp : map.entrySet()) {
			Return.add(new Node(temp.getKey(), temp.getValue()));
		}

		return Return;
	}

//---------------------------------------------------------------------------------------------------------------
	static Node createTree(List<Node> list) {
		while (list.size() > 1) {
			Collections.sort(list);
			Node left = list.get(0);
			Node right = list.get(1);
			Node parent = new Node(null, left.getFrequency() + right.getFrequency());
			parent.setLeft(left);
			parent.setRight(right);
			list.remove(0);
			list.remove(0);
			list.add(parent);
		}
		return list.get(0);
	}

//---------------------------------------------------------------------------------------------------------------
	static HashMap<Byte, String> getRoot(Node node) {
		if (node == null)
			return null;
		getRoot(node.getLeft(), "0", stringBuilder);
		getRoot(node, "1", stringBuilder);
		return root;
	}

	static void getRoot(Node node, String bit, StringBuilder stringBuilder) {
		StringBuilder temp = new StringBuilder(stringBuilder);
		temp.append(bit);
		if (node != null) {
			if (node.getData() == null) {
				getRoot(node.getLeft(), "0", temp);
				getRoot(node.getRight(), "1", temp);
			} else {
				root.put(node.getData(), temp.toString());
			}
		}
	}

//---------------------------------------------------------------------------------------------------------------
	static byte[] convertStringToByte(byte[] bytes, HashMap<Byte, String> Codes) {
		int length, index = 0;
		StringBuilder tempBuilder = new StringBuilder();
		String temp;
		byte[] Return;

		for (int i = 0; i < bytes.length; i++) {
			tempBuilder.append(Codes.get(bytes[i]));
		}

		if (tempBuilder.length() % 8 == 0) {
			length = (tempBuilder.length() / 8);
		} else {
			length = (tempBuilder.length() / 8) + 1;
		}

		Return = new byte[length];
		for (int i = 0; i < tempBuilder.length(); i = (i + 8)) {

			if (tempBuilder.length() < i + 8) {
				temp = tempBuilder.substring(i);
			} else {
				temp = tempBuilder.substring(i, (i + 8));
			}
			Return[index] = (byte) Integer.parseInt(temp, 2);
			index++;

		}
		return Return;
	}

//------------------------------------------decode---------------------------------------------------------------
	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		try {
			InputStream inputstream = new FileInputStream(input_names[0]);
			ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
			byte[] fileBytes = (byte[]) objectinputstream.readObject();
			HashMap<Byte, String> map = (HashMap<Byte, String>) objectinputstream.readObject();
			byte[] Reault = BuildFile(map, fileBytes);
			OutputStream outputstream = new FileOutputStream(output_names[0]);
			outputstream.write(Reault);
			outputstream.close();
			objectinputstream.close();
			inputstream.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

//---------------------------------------------------------------------------------------------------------------
	public static byte[] BuildFile(HashMap<Byte, String> EnrtyMap, byte[] bitFile) {
		int count;
		HashMap<String, Byte> map = new HashMap<String, Byte>();

		List<Byte> list = new ArrayList<>();
		byte[] byteFromList;

		for (int i = 0; i < bitFile.length; i++) {
			stringBuilder.append(convertByteToString(!(i == bitFile.length - 1), bitFile[i]));
		}

		for (HashMap.Entry<Byte, String> entry : EnrtyMap.entrySet()) {
			map.put(entry.getValue(), entry.getKey());
		}

		for (int i = 0; i < stringBuilder.length(); i = i + count) {
			count = 0;
			while (true) {
				try {

					Byte Temp = map.get(stringBuilder.substring(i, (i + count)));
					if (Temp == null) {
						count++;
					} else {
						list.add(Temp);
						break;
					}
				} catch (Exception e) {
					Byte Temp = map.get(stringBuilder.substring(i, stringBuilder.length()));
					break;
				}
			}
		}
		byteFromList = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			byteFromList[i] = list.get(i);
		}
		return byteFromList;
	}

//---------------------------------------------------------------------------------------------------------------
	public static String convertByteToString(boolean flag, byte bit) {
		int temp = bit;
		if (flag) {
			temp = temp + 256;
		}

		String Return = Integer.toBinaryString(temp);

		if (flag) {
			return Return.substring(Return.length() - 8);
		} else {
			return "0"+Return;
		}
	}
}
//---------------------------------------------------------------------------------------------------------------
