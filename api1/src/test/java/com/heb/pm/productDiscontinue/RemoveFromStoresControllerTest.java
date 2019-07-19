package com.heb.pm.productDiscontinue;

import com.heb.util.controller.ModifiedEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m594201 on 9/22/2016.
 */
public class RemoveFromStoresControllerTest {

    @InjectMocks
    private RemoveFromStoresController removeFromStoresController = new RemoveFromStoresController();

    @Mock
    private RemoveFromStoresService removeFromStoresService;

    @Mock
    private MessageSource messageSource;

    HttpServletRequest mockedRequest;

    List<RemoveFromStores> testList;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        this.mockedRequest = Mockito.mock(HttpServletRequest.class);

        testList = new ArrayList<>();

        RemoveFromStores test = new RemoveFromStores();
        test.setUpc((long) 3);
        test.setRemovedInStores(true);
        testList.add(0,test);

    }

    @Test
    public void testRemoveFromStore() throws Exception {

        ModifiedEntity<List<RemoveFromStores>> removeFromStores;

        Mockito.when(this.messageSource.getMessage("Update successful", null, "Remove from Stores Successful", this.mockedRequest.getLocale())).thenReturn("Success");

        Mockito.doReturn(testList).when(this.removeFromStoresService).update(testList);

        removeFromStores = removeFromStoresController.removeFromStore(testList, mockedRequest);

        Assert.notNull("Null Value", removeFromStores.toString());

    }

    @Test
    public void testRemoveFromStoreFailedResponse(){

        ModifiedEntity<List<RemoveFromStores>> removeFromStores;

        Mockito.when(this.messageSource.getMessage("Update successful", null, "Remove from Stores Successful", this.mockedRequest.getLocale())).thenReturn("Success");

        Mockito.doReturn(testList).when(this.removeFromStoresService).update(testList);

        removeFromStores = removeFromStoresController.removeFromStore(testList, mockedRequest);

        Assert.notNull("Null Value", removeFromStores.toString());

    }

}