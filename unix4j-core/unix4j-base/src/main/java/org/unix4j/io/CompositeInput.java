package org.unix4j.io;

import org.unix4j.line.Line;

import java.util.Arrays;
import java.util.List;

/**
 * An input composed from multiple other inputs returning all lines of the first
 * input, then all lines of the of the second input etc.
 */
public class CompositeInput extends AbstractInput {

	private int index;
	private final List<? extends Input> inputs;

	/**
	 * Constructor with inputs to combine.
	 * 
	 * @param inputs
	 *            the inputs to combine
	 */
	public CompositeInput(Input... inputs) {
		this(Arrays.asList(inputs));
	}

	/**
	 * Constructor with inputs to combine.
	 * 
	 * @param inputs
	 *            the inputs to combine
	 */
	public CompositeInput(List<? extends Input> inputs) {
		this.inputs = inputs;
	}

	@Override
	public boolean hasMoreLines() {
		while (index < inputs.size()) {
			if (inputs.get(index).hasMoreLines()) {
				return true;
			}
			index++;
		}
		return false;
	}

	@Override
	public Line readLine() {
		if (hasMoreLines()) {
			return inputs.get(index).readLine();
		}
		return null;
	}

	/**
	 * Invokes close of all component input objects.
	 */
	@Override
	public void close() {
		for (final Input input : inputs) {
			input.close();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(inputs=" + inputs + ")";
	}
}
