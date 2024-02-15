package edu.br.ufpe.cin.sword.cm.fol.model.terms;

public class FOLTermFactory {

    public static FOLVariable variable(String name) {
        return new FOLVariable(name);
    }

    public static FOLFunction function(String name, FOLTerm... terms) {
        return new FOLFunction(name, terms);
    }

    public static FOLFunction groundTerm(String name) {
        return new FOLFunction(name);
    }

}
