package com.fomywo.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.functors.MapTransformer;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.fomywo.annotation.fomywoTransformation;
import com.fomywo.tools.ReturnContainer;
import com.fomywo.wordAction.description.FomywoTransformation;
import com.fomywo.wordAction.impl.DateType;

public class MainFactory {
	private Logger log = Logger.getLogger("famywo.main");
	private Map<String, FomywoTransformation> collect;

	public ReturnContainer action(String order) {
		log.info("Lancement :::");
		ReturnContainer container = new ReturnContainer();
		init();
		callAllTransformations(container, order);
		return container;
	}

	private void callAllTransformations(ReturnContainer container, String order) {

		// split order base on uppercase
		List<String> splittedOrder = Arrays.asList(order.replaceAll("(\\p{Upper})", "_$1").split("_"));
		log.info(splittedOrder.toString());
		splittedOrder
		.stream()
		.filter(x -> {
			log.info("FILTER __________________ "+ x + " : " + (collect.get(x) != null)  + " -- "  + isTransformation(collect.get(x)));
			return (collect.get(x) != null) && isTransformation(collect.get(x));
		})
		.forEach(x -> {
					log.info(x 
							+ " : " 
							+ (collect.get(x)) 
							+ " -- " 
							//+isTransformation(collect.get(x))
							);
					}
		);
//		.forEach(key -> {
//			log.info("transformation " + key);
//		});

	}

	/**
	 * scan pkg to find all tools
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		Reflections ref = new Reflections(new TypeAnnotationsScanner());
		Set annotedAsFomywoTransformations = ref.getTypesAnnotatedWith(fomywoTransformation.class);
		try {
			this.collect = (Map<String, FomywoTransformation>) annotedAsFomywoTransformations.stream()
			// no use of colision resolver, because if there is 2
			// fomytransformation we need to throw exception
					.collect(Collectors.toMap(this::mapName, fomywoTransformation -> fomywoTransformation));

		} catch (IllegalStateException e) {
			log.log(Level.SEVERE, "Several instance of the same FomywoTransformation founded ", e);
			e.printStackTrace();
		}
	}

	private String mapName(Class<?> in) {
		String simpleName = in.getName();
		return simpleName.substring(simpleName.lastIndexOf('.') + 1, simpleName.indexOf("Type"));
	}

	private Boolean isTransformation(Object input) {
		if(input == null)
			return false;
		return Arrays.asList(input.getClass().getInterfaces()).contains(FomywoTransformation.class);
	}

	public static void main(String[] args) {
		MainFactory main = new MainFactory();

		main.action("aaa45ErrDateRrrr");

		// DateType date = new DateType();
		// System.out.println(date instanceof fomywoTransformation);
		// System.out.println(Arrays.asList(date.getClass().getInterfaces()).contains(FomywoTransformation.class));

	}
}
