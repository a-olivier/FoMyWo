package com.fomywo.wordAction.impl;

import org.joda.time.DateTime;

import com.fomywo.annotation.fomywoTransformation;
import com.fomywo.tools.ReturnContainer;
import com.fomywo.wordAction.description.IFomywoTransformation;

@fomywoTransformation
public class DateType  implements IFomywoTransformation<DateTime> {


	public DateTime action (String command)
	{
		System.out.println("lancement de date");
		return null;
	}
	
	
}
