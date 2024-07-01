package edu.br.ufpe.cin.sword.cm.alchb;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbUnaryIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.alchb.visitor.LaTeXMatrixVisitor;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.tree.StartProofTree;

public class ALCHbProverTest {
	
	private static ALCHbFactory factory;
	private static ALCHbSimpleProver prover;
	
	@BeforeClass
	public static void prepare() {
		factory = new ALCHbFactory();
		prover = new ALCHbSimpleProver();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void simpleTests() {
		ALCHbIndividual a = factory.ind("a");
		
		ProofTree<ALCHbLiteral> proof = null;
		
		proof = prover.prove(factory.matrixOf(
			factory.clauseOf(factory.conLiteral("A", false, a)),
			factory.clauseOf(factory.conLiteral("A", true, a))
		));
		
		assertTrue(proof instanceof StartProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(factory.conLiteral("A", true, a)),
				factory.clauseOf(factory.conLiteral("A", true, a))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, a),
						factory.conLiteral("B", false, a)
				),
				factory.clauseOf(factory.conLiteral("A", false, a))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, a),
						factory.conLiteral("B", false, a)
				),
				factory.clauseOf(factory.conLiteral("A", false, a)),
				factory.clauseOf(factory.conLiteral("B", true, a))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		
		ALCHbVariable x = factory.var("x");
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, x),
						factory.conLiteral("B", false, x)
				),
				factory.clauseOf(factory.conLiteral("A", false, a)),
				factory.clauseOf(factory.conLiteral("B", true, factory.ind("b")))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("x"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true, factory.ind("b")))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("x"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true, factory.ind("a")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("y"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true, factory.ind("b")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("y"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true, factory.var("z")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("x"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true, factory.unaryInd("f", factory.var("x"))))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.var("x"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.unaryInd("f", factory.var("x")))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.unaryInd("f", factory.var("x"))))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("y")),
						factory.conLiteral("B", false, factory.var("y"))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.unaryInd("f", factory.var("x")))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.unaryInd("f", factory.var("x"))))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.var("y"))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.var("z")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.var("z")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.var("z"))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.ind("a")))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a")),
								factory.conLiteral("C", true,  factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("C", false,  factory.var("z"))),
				factory.clauseOf(factory.conLiteral("B", true,  factory.var("y")))
		));
			
		assertTrue(proof instanceof StartProofTree<?>);
		System.out.println(proof);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.ind("a")),
								factory.conLiteral("B", true,  factory.ind("a"))),
				factory.clauseOf(factory.conLiteral("B", false,  factory.var("x")),
								factory.conLiteral("A", true,  factory.var("x")))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(factory.matrixOf(
				factory.clauseOf(
						factory.conLiteral("A", true, factory.var("x")),
						factory.conLiteral("B", false, factory.unaryInd("f", factory.var("x")))
				),
				factory.clauseOf(factory.conLiteral("A", false, factory.var("x")),
								factory.conLiteral("B", true,  factory.var("x"))),
				factory.clauseOf(factory.conLiteral("B", false,  factory.var("y")),
								factory.conLiteral("A", true,  factory.var("y")))
		));
			
		assertTrue(proof instanceof FailProofTree<?>);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testPaperExample() {		
		ALCHbVariable[] x = {
			factory.var("x_0"),
			factory.var("x_1"),
			factory.var("x_2"),
			factory.var("x_3"),
			factory.var("x_4"),
			factory.var("x_5"),
			factory.var("x_6"),
			factory.var("x_7"),
			factory.var("x_8"),
			factory.var("x_9"),
			factory.var("x_{10}"),
			factory.var("x_{11}"),
			factory.var("x_{12}"),
			factory.var("x_{13}"),
			factory.var("x_{14}"),
			factory.var("x_{15}"),
			factory.var("x_{16}"),
			factory.var("x_{17}"),
			factory.var("x_{18}"),
			factory.var("x_{19}"),
			factory.var("x_{20}"),
			factory.var("x_{21}"),
			factory.var("x_{22}")
		};
		
		ALCHbIndividual a = factory.ind("a");
		ALCHbIndividual b = factory.ind("b");
		ALCHbUnaryIndividual f_x_5 = factory.unaryInd("c", x[5]);
		ALCHbUnaryIndividual f_x_6 = factory.unaryInd("c", x[6]);
		ALCHbUnaryIndividual f_x_7 = factory.unaryInd("c", x[7]);
		
		ALCHbUnaryIndividual g_x_9 = factory.unaryInd("d", x[9]);
		ALCHbUnaryIndividual g_x_11 = factory.unaryInd("d", x[11]);
		ALCHbUnaryIndividual g_x_13 = factory.unaryInd("d", x[13]);
		
		ALCHbUnaryIndividual h_x_10 = factory.unaryInd("e", x[10]);
		ALCHbUnaryIndividual h_x_12 = factory.unaryInd("e", x[12]);
		ALCHbUnaryIndividual h_x_14 = factory.unaryInd("e", x[14]);
		
		List<List<ALCHbLiteral>> matrix = factory.matrixOf(
			// \exists s . B \sub N
			factory.clauseOf(
					factory.roleLiteral("s", true, x[0], x[1]),
					factory.conLiteral("B", true, x[1]),
					factory.conLiteral("N", false, x[0])),
			// N \sub \pf A
			factory.clauseOf(
					factory.conLiteral("N", true, x[2]),
					factory.conLiteral("A", false, x[2])
			),
			factory.clauseOf(
					factory.conLiteral("N", true, x[3]),
					factory.conLiteral("A", true, x[4]),
					factory.ordLiteral(true, x[4], x[3])
			),
			// \pf A \sub N
			factory.clauseOf(
					factory.conLiteral("A", true, x[5]),
					factory.conLiteral("N", false, x[5]),
					factory.ordLiteral(false, f_x_5, x[5])
			),
			factory.clauseOf(
					factory.conLiteral("A", true, x[6]),
					factory.conLiteral("N", false, x[6]),
					factory.conLiteral("A", false, f_x_6)
			),
			factory.clauseOf(
					factory.conLiteral("A", true, x[7]),
					factory.conLiteral("N", false, x[7]),
					factory.conLiteral("A", true, x[8]),
					factory.ordLiteral(true, x[8], f_x_7)
			),
			// \pf r \sub s
			factory.clauseOf(
					factory.roleLiteral("r", true, x[9], x[10]),
					factory.roleLiteral("s", false, x[9], x[10]),
					factory.biOrdLiteral(false, g_x_9, h_x_10, x[9], x[10])
			),
			factory.clauseOf(
					factory.roleLiteral("r", true, x[11], x[12]),
					factory.roleLiteral("s", false, x[11], x[12]),
					factory.roleLiteral("r", false, g_x_11, h_x_12)
			),
			factory.clauseOf(
					factory.roleLiteral("r", true, x[13], x[14]),
					factory.roleLiteral("s", false, x[13], x[14]),
					factory.roleLiteral("r", true, x[15], x[16]),
					factory.biOrdLiteral(true, x[15], x[16], g_x_13, h_x_14)
			),
			// N_r \sub \pf r
			factory.clauseOf(
					factory.roleLiteral("N_r", true, x[17], x[18]),
					factory.roleLiteral("r", false, x[17], x[18])
			),			
			factory.clauseOf(
					factory.roleLiteral("N_r", true, x[19], x[20]),
					factory.roleLiteral("r", true, x[21], x[22]),
					factory.biOrdLiteral(true, x[21], x[22], x[19], x[20])
			),
			// N_r (a, b)
			factory.clauseOf(
					factory.roleLiteral("N_r", false, a, b)
			),
			// B (b)
			factory.clauseOf(
					factory.conLiteral("B", false, b)
			),
			// \models A(a)
			factory.clauseOf(
					factory.conLiteral("A", true, a)
			)			
		);
		
		ProofTree<ALCHbLiteral> proof = prover.prove(matrix);
		
		assertTrue(proof instanceof StartProofTree<?>);
		Collections.reverse(matrix);
		System.out.println(LaTeXMatrixVisitor.<ALCHbLiteral>getLatexMatrixOf(matrix));
		System.out.println(proof.latexString());
	}

}
