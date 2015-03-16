package com.fomywo.tools;

import com.fomywo.wordAction.description.FomywoTransformation;

public class ReturnContainer<T> {

	private Object returnContent;
	private FomywoTransformation<T> transformation;
	private ReturnContainer<T> next;
	private ReturnContainer<T> previous;
	private String order; 

	/**
	 * 
	 * GETTER / SETTER
	 * 
	 */
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Object getReturnContent() {
		return returnContent;
	}

	public void setReturnContent(Object returnContent) {
		this.returnContent = returnContent;
	}

	public ReturnContainer<T> getNext() {
		return next;
	}

	public void setNext(ReturnContainer<T> next) {
		this.next = next;
	}

	public ReturnContainer<T> getPrevious() {
		return previous;
	}

	public void setPrevious(ReturnContainer<T> previous) {
		this.previous = previous;
	}

	public boolean isLast() {
		return next == null;
	}

	/*--------------------------------------*/

	private void setToLast(){
		if(! isLast()){
			this.next.setPrevious(null);
			this.setNext(null);
		}
	}
	
	private ReturnContainer<T> getLast() {
		ReturnContainer<T> scannedRC = this;

		while (!scannedRC.isLast()) {
			scannedRC = scannedRC.getNext();
		}
		return scannedRC;
	}

	protected String showTransformationChain() {
		String chain = "";
		chain = transformation.getClass().getName() + " ";
		if (!this.isLast()) {
			chain.concat(this.getNext().showTransformationChain());
		}
		return chain;
	}

	public void add(FomywoTransformation<T> transformation) {
		if (this.isLast()) {
			this.transformation = transformation;
		} else {
			this.next.add(transformation);
		}
	}

	private void transform(){
		this.transformation.action(order);
	}
	
	public void launch(){
		ReturnContainer<T> last = getLast(); 
		
		
		
		
	}
	
}
