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

/**
 * Assignment 1 Submitted by: Javier Giberg. ID# 302280383 Netanel Bitton. ID#
 * 305484651
 */

public class HuffmanEncoderDecoder implements Compressor {

	private static Map<Byte, String> Compress;
	private static Map<Byte, String> root;
	private static StringBuilder stringBuilder;

	public HuffmanEncoderDecoder() {
		Compress = new HashMap<>();
		root = new HashMap<>();
		stringBuilder = new StringBuilder();
	}

//-------------------------------------------------Compress------------------------------------------------------
	public void Compress(String[] input_names, String[] output_names) {
		try {
			// input and read file
			FileInputStream fileinputstream = new FileInputStream(input_names[0]);
			byte[] fileBytes = new byte[fileinputstream.available()];
			fileinputstream.read(fileBytes);
			// build array byte
			byte[] Reault = BuildCompress(fileBytes);
			// save file compress
			OutputStream outputstream = new FileOutputStream(output_names[0]);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
			// write byte code
			objectoutputstream.writeObject(Reault);
			// write HashMap
			objectoutputstream.writeObject(Compress);
			objectoutputstream.close();
			outputstream.close();
			fileinputstream.close();
		} catch (Exception e) {
			System.err.println("Excepptio from \"Compress\"- line 35-57");
		}
	}

//---------------------------------------------------------------------------------------------------------------
	static byte[] BuildCompress(byte[] fileBytes) {
		List<Node> list = getList(fileBytes);
		Node root = createTree(list);
		root.Order();
		Compress = getRoot(root);
		return conversionStringToByte(fileBytes, Compress);

	}

//---------------------------------------------------------------------------------------------------------------
	static List<Node> getList(byte[] contentbytes) {
		ArrayList<Node> Return = new ArrayList<Node>();
		Map<Byte, Integer> map = new HashMap<>();
		for (byte temp : contentbytes) {
			Integer count = map.get(temp);
			if (count == null) {
				map.put(temp, 1);
			} else {
				map.put(temp, count + 1);
			}
		}
		for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
			Return.add(new Node(entry.getKey(), entry.getValue()));
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

//------------------F---------------------------------------------------------------------------------------------
	static Map<Byte, String> getRoot(Node node) {
		if (node == null) return null;
		getCodes(node.getLeft(), "0", stringBuilder);
		getCodes(node, "1", stringBuilder);
		return root;
	}

	static void getCodes(Node node, String bit, StringBuilder stringBuilder) {
		StringBuilder stringBuilder2 = new StringBuilder(stringBuilder);
		stringBuilder2.append(bit);
		if (node != null) {
			if (node.getData() == null) {
				getCodes(node.getLeft(), "0", stringBuilder2);
				getCodes(node.getRight(), "1", stringBuilder2);
			} else {
				root.put(node.getData(), stringBuilder2.toString());
			}
		}
	}

//---------------------------------------------------------------------------------------------------------------
	static byte[] conversionStringToByte(byte[] bytes, Map<Byte, String> Codes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte bYte : bytes) {
			stringBuilder.append(Codes.get(bYte));
		}
		int length = 0;
		if (stringBuilder.length() % 8 == 0) {
			length = stringBuilder.length() / 8;
		} else {
			length = stringBuilder.length() / 8 + 1;
		}
		int index = 0;
		byte[] Byte = new byte[length];
		for (int i = 0; i < stringBuilder.length(); i = i + 8) {
			String temp;
			if (stringBuilder.length() < i + 8) {
				temp = stringBuilder.substring(i);
			} else {
				temp = stringBuilder.substring(i, i + 8);
			}
			Byte[index] = (byte) Integer.parseInt(temp, 2);
			index++;
		}
		return Byte;
	}

//------------------------------------------decode---------------------------------------------------------------
	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		try {
			InputStream inputstream = new FileInputStream(input_names[0]);
			ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
			byte[] huffmanBytes = (byte[]) objectinputstream.readObject();
			Map<Byte, String> huffmanMap = (Map<Byte, String>) objectinputstream.readObject();
			byte[] Reault = decode(huffmanMap, huffmanBytes);
			OutputStream outputstream = new FileOutputStream(output_names[0]);
			outputstream.write(Reault);
			outputstream.close();
			objectinputstream.close();
			inputstream.close();
		} catch (Exception e) {
			System.err.println("Excepptio from \"Decompress\"- line 148-172");
		}
	}

//---------------------------------------------------------------------------------------------------------------
	public static byte[] decode(Map<Byte, String> huffmanCodes, byte[] huffmanCodesByte) {
		StringBuilder stringBuilder1 = new StringBuilder();
		for (int i = 0; i < huffmanCodesByte.length; i++) {
			byte b = huffmanCodesByte[i];
			boolean flag = (i == huffmanCodesByte.length - 1);
			stringBuilder1.append(convertByteToString(!flag, b));
		}
		Map<String, Byte> map = new HashMap<String, Byte>();
		for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
			map.put(entry.getValue(), entry.getKey());
		}
		List<Byte> list = new ArrayList<>();
		for (int i = 0; i < stringBuilder1.length();) {
			int count = 0;
			boolean flag = true;
			while (flag) {
				String search = stringBuilder1.substring(i, i + count);
				Byte b = map.get(search);
				if (b == null) {
					count++;
				} else {
					flag = false;
					list.add(b);
				}
			}
			i = i + count;
		}
		byte[] listToByte = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			listToByte[i] = list.get(i);
		}
		return listToByte;
	}

//---------------------------------------------------------------------------------------------------------------
	public static String convertByteToString(boolean flag, byte bit) {
		int temp = bit;
		if (flag) {
			temp |= 256;
		}
		String Return = Integer.toBinaryString(temp);
		if (flag) {
			return Return.substring(Return.length() - 8);
		} else {
			return Return;
		}
	}
}
//---------------------------------------------------------------------------------------------------------------
