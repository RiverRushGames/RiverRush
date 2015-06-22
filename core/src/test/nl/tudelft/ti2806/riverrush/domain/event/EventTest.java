package nl.tudelft.ti2806.riverrush.domain.event;

import nl.tudelft.ti2806.riverrush.network.protocol.BasicProtocol;
import nl.tudelft.ti2806.riverrush.network.protocol.InvalidProtocolException;
import nl.tudelft.ti2806.riverrush.network.protocol.Protocol;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Abstract test for every class that implements {@link Event}
 */
public abstract class EventTest {

    protected Event event;
    protected Protocol protocol = new BasicProtocol(0);
    protected static ArrayList<String> uglyList = new ArrayList<>();


    @BeforeClass
    public static void getParameters() {
        uglyList = new ArrayList<>();
    }

    @Test
    public void testDeserialize() throws Exception {
        event = getInstance();

        Event returnEvent = event.deserialize(getTestMap());
        assertEquals(new Integer(getAnimalId()), returnEvent.getAnimal());
    }

    @Test
    public void testDeserializeMissingParameters()
    {
        for(String s : uglyList) {
            try {
                Map<String,String> testMap = getTestMap();
                testMap.remove(s);
                event = getInstance();
                event.deserialize(testMap);
                fail("When removing " + s + " it will still deserialize");
            } catch (InvalidProtocolException ignored) {

            }
        }
    }

    public void addVariables(Event event) {
        event.setAnimal(getAnimalId());
    }

    public Event getInstance(){
        return event;
    }

    public Map<String,String> getTestMap() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("animal", Integer.toString(getAnimalId()));
        return testMap;
    }

    public int getAnimalId() {
        return 10;
    }


}
