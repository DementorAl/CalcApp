package ru.danilov.calcapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTest {
    MatchParser parser =new MatchParser();

    @Test
    public void main_Test() throws Exception{
        assertEquals(-226.2272,parser.Parse("1+2*5 - 5/22 + (45 +34)*-3"),0.0001);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, parser.Parse("2+2"),0.0001);
    }
    @Test
    public void subtraction_isCorrect() throws Exception {
        assertEquals(-111,parser.Parse("854-965"),0.0001);
    }
    @Test
    public void multiplication_isCorrect() throws Exception {
        assertEquals(774, parser.Parse("3*258"),0.0001);
    }
    @Test
    public void division_isCorrect() throws Exception {
        assertEquals(18.5185, parser.Parse("500/27"),0.0001);
    }
    @Test
    public void addition_Double_isCorrect() throws Exception {
        assertEquals(-757.98, parser.Parse("-854.78+96.8"),0.0001);
    }
    @Test
    public void subtraction_Double_isCorrect() throws Exception {
        assertEquals(-2.1648,parser.Parse("7.4852-9.65"),0.0001);
    }
    @Test
    public void multiplication_Double_isCorrect() throws Exception {
        assertEquals(385704.6876, parser.Parse("452.3578*852.654"),0.0001);
    }

    @Test
    public void division_Double_isCorrect() throws Exception {
        assertEquals(10.38156, parser.Parse("159.357/15.35"),0.0001);
    }


    @Test
    public void addition_RPN_Test() throws Exception{
        String exp =  parser.normalizeRPNExp("2 2 +");

        assertEquals(4,(parser.Parse(parser.ParseRPN(exp))),0.0001);
    }

    @Test
    public void subtraction_RPN_Test() throws Exception{
        String exp =  parser.normalizeRPNExp("741 852 -");

        assertEquals(-111,(parser.Parse(parser.ParseRPN(exp))),0.0001);
    }

    @Test
    public void multiplication_RPN_Test() throws Exception{
        String exp =  parser.normalizeRPNExp("987 2 *");

        assertEquals(1974,(parser.Parse(parser.ParseRPN(exp))),0.0001);
    }

    @Test
    public void division_RPN_Test() throws Exception{
        String exp =  parser.normalizeRPNExp("16 5 /");

        assertEquals(3.2,(parser.Parse(parser.ParseRPN(exp))),0.0001);
    }

    @Test
    public void main_RPN_Test() throws Exception{
        String exp =  parser.normalizeRPNExp("2 8 * 9 4 / - 75.7 58 - 2 * +");

        assertEquals(49.15,(parser.Parse(parser.ParseRPN(exp))),0.0001);
    }

}