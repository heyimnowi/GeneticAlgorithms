package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import model.Item;
import model.ItemSet;

public class EquipmentReader {

	private static EquipmentReader _instance;

	public static EquipmentReader instance() {
		if (_instance == null) {
			try {
				 synchronized (EquipmentReader.class) {
					 if(_instance == null) {
						 EquipmentReader._instance = new EquipmentReader();
					 }
				 }
			} catch (Exception e) {
				throw new RuntimeException("Error while reading equipments", e);
			}
		}
		return _instance;
	}

	private final List<ItemSet> itemSets;
	
	private EquipmentReader() throws InterruptedException, ExecutionException {
		List<String> filenames = Props.instance().equipmentFiles();
		String directory = Props.instance().getEquipmentDirectory();
		this.itemSets = Collections.synchronizedList(new ArrayList<>());
		ExecutorService executor = Executors.newFixedThreadPool(filenames.size());
		CompletionService<String> completionService = 
				new ExecutorCompletionService<>(executor);
		
		for (String filename : filenames) {
			completionService.submit(() -> {
				try (
					InputStream inStream = new FileInputStream(directory + "/" + filename);
					InputStreamReader isr = new InputStreamReader(inStream, Charset.forName("UTF-8"));
					BufferedReader reader = new BufferedReader(isr)
				) {
					String itemType = filename.split("\\.")[0];
					List<Item> items = reader.lines().skip(1).map(line -> {
						List<Double> it = Arrays.stream(line.split("\t"))
								.map(Double::parseDouble)
								.collect(Collectors.toList());
							return new Item(itemType, it.get(0).intValue(), it.get(1), 
									it.get(2), it.get(3), it.get(4), it.get(5));
						}).collect(Collectors.toList());
					this.itemSets.add(new ItemSet(items, itemType));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return filename;
			});
		}
		int taken = 0;
		while (taken++ < filenames.size()) {
			System.out.println("Finished reading: " + completionService.take().get());
		}
		executor.shutdown();
	}
	
	public List<ItemSet> getItemSets() {
		return itemSets;
	}
}
