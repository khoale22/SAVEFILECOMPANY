package com.heb.pm.productDiscontinue;

import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Remove from stores service.
 *
 * @author m594201
 * @since 2.0.4
 */
@Service
public class RemoveFromStoresService {

    @Autowired
    private ProductManagementServiceClient productManagementServiceClient;

    /**
     * Update. Iterates through a list and calls webservice.
     *
     * @param returnList list of upc and proc_scn_maint_sw.
     */
    public List<RemoveFromStores> update(List<RemoveFromStores> returnList){

        for (RemoveFromStores removeFromStores : returnList) {
            try{
                this.productManagementServiceClient.submitRemoveFromStoresSwitch(removeFromStores);
                removeFromStores.setSuccessful(true);
            } catch (CheckedSoapException e) {
                removeFromStores.setSuccessful(false);
                removeFromStores.setErrorMessage(e.getMessage());
            }
        }

        return returnList;

    }
}
