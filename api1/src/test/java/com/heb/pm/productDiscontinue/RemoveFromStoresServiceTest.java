package com.heb.pm.productDiscontinue;

import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by m594201 on 9/23/2016.
 */
public class RemoveFromStoresServiceTest {

    @InjectMocks
    private RemoveFromStoresService removeFromStoresService;

    @Mock
    private ProductManagementServiceClient productManagementServiceClient;

    private List<RemoveFromStores> testList;

    private List<RemoveFromStores> returnedMessage;

    RemoveFromStores test;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        this.testList = new ArrayList<>();

        test = new RemoveFromStores();
        test.setUpc((long) 3);
        test.setRemovedInStores(true);
        this.testList.add(0,test);

    }

    @Test
    public void testUpdate() throws CheckedSoapException {

        Mockito.when(this.productManagementServiceClient.submitRemoveFromStoresSwitch(test)).thenReturn("Success");

        returnedMessage = this.removeFromStoresService.update(this.testList);

    }

}