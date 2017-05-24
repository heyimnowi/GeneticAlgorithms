package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import model.Item;
import model.ItemSet;

public class EquipmentReader {

	private static EquipmentReader _instance;

	public static EquipmentReader instance() {
		if (_instance == null) {
			try {
				EquipmentReader._instance = new EquipmentReader();
			} catch (IOException e) {
				throw new RuntimeException("Error while reading equipments", e);
			}
		}
		return _instance;
	}

	private final List<ItemSet> itemSets;
	
	private EquipmentReader() throws FileNotFoundException, IOException {
		List<String> filenames = Props.instance().equipmentFiles();
		String directory = Props.instance().getEquipmentDirectory();
		this.itemSets = new ArrayList<>();
		for (String filename : filenames) {
			try (
				InputStream inStream = new FileInputStream(directory + "/" + filename);
				InputStreamReader isr = new InputStreamReader(inStream, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(isr)
			) {
				List<Item> items = reader.lines().skip(1).map(line -> {
					List<Double> it = Arrays.stream(line.split("\t"))
							.map(Double::parseDouble)
							.collect(Collectors.toList());
						return new Item(it.get(0).intValue(), it.get(1), 
								it.get(2), it.get(3), it.get(4), it.get(5));
					}).collect(Collectors.toList());
				this.itemSets.add(new ItemSet(items, filename.split("\\.")[0]));
			}
		}
	}
	
	public List<ItemSet> getItemSets() {
		return itemSets;
	}
}
