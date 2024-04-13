package edu.br.ufpe.cin.sword.cm.alchb.visitor;

import java.util.List;
import java.util.stream.Collectors;

public class LaTeXMatrixVisitor {
	
	public static <Literal> String  getLatexMatrixOf(List<List<Literal>> matrix) {
		
		String prefix = "$\\cmatrix{";
		String suffix = "}{}$";
		
		return prefix + "\n" + matrix.stream()
			.map(clause -> clause.stream()
					.map(literal -> literal.toString().replace("<<", "\\ll"))
					.collect(Collectors.joining(" \\& ")))
			.collect(Collectors.joining(" \\\\\n"))
			+ "\\\\\n" + suffix;
		
	}

}
