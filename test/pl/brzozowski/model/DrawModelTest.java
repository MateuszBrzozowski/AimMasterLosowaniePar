package pl.brzozowski.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrawModelTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private DrawModel drawModel =  new DrawModel();

    @Test
    public void draft_SameKD_EqulsList(){
        //given
        drawModel.getPresentPlayers().add(new Player("test1",2));
        drawModel.getPresentPlayers().add(new Player("test2",2));
        drawModel.getPresentPlayers().add(new Player("test3",2));
        drawModel.getPresentPlayers().add(new Player("test4",2));
        drawModel.getPresentPlayers().add(new Player("test5",2));
        drawModel.getPresentPlayers().add(new Player("test6",2));
        //when
        drawModel.draft();
        //then
        Assertions.assertEquals(drawModel.getAboveKDPlayers().size(),drawModel.getBelowKDPlayers().size());
    }

}