package com.fomywo.factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.fomywo.annotation.fomywoTransformation;
import com.fomywo.tools.ReturnContainer;
import com.fomywo.wordAction.description.IFomywoTransformation;

public class MainFactory {
	private final Logger log = Logger.getLogger("famywo.main");
	@SuppressWarnings("rawtypes")
	private Map<String, IFomywoTransformation> mapTransfoKeyWords;

	public ReturnContainer action(String order) {
		log.info("Lancement :::");
		ReturnContainer container = new ReturnContainer();
		init();
		// callAllTransformations(container, order);
		return container;
	}

	private void callAllTransformations(ReturnContainer container, String order) {

		// split order base on uppercase
		List<String> splittedOrder = Arrays.asList(order.replaceAll("(\\p{Upper})", "_$1").split("_"));
		log.info(splittedOrder.toString());
		splittedOrder.stream().filter(x -> {
			return mapTransfoKeyWords.get(x) != null;
		}).forEach(z -> {
			container.add(((IFomywoTransformation) mapTransfoKeyWords.get(z)));
		});

		log.info(container.showTransformationChain());
		// .forEach(key -> {
		// log.info("transformation " + key);
		// });

	}

	/**
	 * scan pkg to find all tools
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		Reflections ref = new Reflections(new TypeAnnotationsScanner());
		Set annotedAsFomywoTransformations = ref.getTypesAnnotatedWith(fomywoTransformation.class);
		try {
			this.mapTransfoKeyWords = (Map<String, IFomywoTransformation>) annotedAsFomywoTransformations.stream()
			// no use of colision resolver, because if there is 2
			// fomytransformation we need to throw exception
					.collect(
							Collectors.toMap(
									this::mapName,
									fomywoTransformation -> {
										// instanciate with default constructor
										Constructor<?>[] constructors = fomywoTransformation.getConstructors();
										Stream<IFomywoTransformation> map = Arrays
												.asList(constructors)
												.stream()
												.filter(constructor -> constructor.getParameters().length == 0)
												.map(defaultConstructor -> {
													IFomywoTransformation transformation = null;
													try {
														transformation = (IFomywoTransformation) defaultConstructor.newInstance();
													} catch (Exception e) {
														log.log(Level.SEVERE, "Cannot instanciate Transformation of "
																+ defaultConstructor.getDeclaringClass().getName(), e);
														e.printStackTrace();
													}
													return transformation;
												});
										Object[] array = map.toArray();
										Assert.assertTrue("More than one constructor found  : " + map.toString(), array.length == 1);
										return array[0];
									}));

			log.log(Level.INFO,"resultat : " + this.mapTransfoKeyWords.toString());
		} catch (IllegalStateException e) {
			log.log(Level.SEVERE, "Several instance of the same FomywoTransformation founded ", e);
			e.printStackTrace();
		}
	}

	private String mapName(Class<?> in) {
		String simpleName = in.getName();
		return simpleName.substring(simpleName.lastIndexOf('.') + 1, simpleName.indexOf("Type"));
	}

	public static void main(String[] args) {
		MainFactory main = new MainFactory();

		main.action("aaa45ErrDateRrrr");

		// DateType date = new DateType();
		// System.out.println(date instanceof fomywoTransformation);
		// System.out.println(Arrays.asList(date.getClass().getInterfaces()).contains(FomywoTransformation.class));

	}
}
