package boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameModelTest {

    BoardGameModel model;

    @BeforeEach
    void init() {
        model = new BoardGameModel();
    }

    @Test
    void move() {
        model.move(0,0);
        assertEquals(Square.PIROS, model.getSquare(0,0));
        model.move(1,1);
        assertEquals(Square.KEK, model.getSquare(1,1));
        assertEquals(Square.PIROS, model.getSquare(0,0));
        assertEquals(Square.NONE, model.getSquare(10,10));
        model.move(1,1);
        assertEquals(Square.KEK, model.getSquare(1,1));
    }

    @Test
    void isFinished_Piros() {
        assertFalse(model.isFinished());
        model.move(1,1);
        assertFalse(model.isFinished());
        model.move(0,2);
        assertFalse(model.isFinished());
        model.move(1,3);
        assertFalse(model.isFinished());
        model.move(0,4);
        assertFalse(model.isFinished());
        model.move(1,5);
        assertFalse(model.isFinished());
        model.move(0,6);
        assertFalse(model.isFinished());
        model.move(1,7);
        assertFalse(model.isFinished());
        model.move(0,8);
        assertFalse(model.isFinished());
        model.move(1,9);
        assertTrue(model.isFinished());
    }

    @Test
    void isFinished_Kek(){
        assertFalse(model.isFinished());
        model.move(0,0);
        assertFalse(model.isFinished());
        model.move(1,1);
        assertFalse(model.isFinished());
        model.move(2,0);
        assertFalse(model.isFinished());
        model.move(3,1);
        assertFalse(model.isFinished());
        model.move(4,0);
        assertFalse(model.isFinished());
        model.move(5,1);
        assertFalse(model.isFinished());
        model.move(6,0);
        assertFalse(model.isFinished());
        model.move(7,1);
        assertFalse(model.isFinished());
        model.move(8,0);
        assertFalse(model.isFinished());
        model.move(9,1);
        assertTrue(model.isFinished());
    }

    @Test
    void isFinished_Draw(){
        assertFalse(model.isFinished());
        model.move(1,1);
        assertFalse(model.isFinished());
        model.move(3,1);
        assertFalse(model.isFinished());
        model.move(3,3);
        assertFalse(model.isFinished());
        model.move(5,3);
        assertFalse(model.isFinished());
        model.move(5,5);
        assertFalse(model.isFinished());
        model.move(7,5);
        assertFalse(model.isFinished());
        model.move(7,7);
        assertFalse(model.isFinished());
        model.move(9,7);
        assertFalse(model.isFinished());
        model.move(9,9);
        assertFalse(model.isFinished());
        model.move(1,3);
        assertTrue(model.isFinished());
    }

    @Test
    void testToString() {
        assertEquals("0 2 0 2 0 2 0 2 0 2 0 \n"
                + "1 0 1 0 1 0 1 0 1 0 1 \n"
                + "0 2 0 2 0 2 0 2 0 2 0 \n"
                + "1 0 1 0 1 0 1 0 1 0 1 \n"
                + "0 2 0 2 0 2 0 2 0 2 0 \n"
                + "1 0 1 0 1 0 1 0 1 0 1 \n"
                + "0 2 0 2 0 2 0 2 0 2 0 \n"
                + "1 0 1 0 1 0 1 0 1 0 1 \n"
                + "0 2 0 2 0 2 0 2 0 2 0 \n"
                + "1 0 1 0 1 0 1 0 1 0 1 \n"
                + "0 2 0 2 0 2 0 2 0 2 0 \n", model.toString());
    }
}