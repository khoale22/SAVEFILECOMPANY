package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.Shipper;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Created by m594201 on 5/22/2017.
 */
public class ShipperResolver implements LazyObjectResolver<Shipper> {
	@Override
	public void fetch(Shipper shipper) {
		shipper.getSellingUnit().getUpc();
		shipper.getSellingUnit().getProductMaster().getProdId();
		if(shipper.getRealUpc() != null){
			shipper.getRealUpc().getUpc();
			shipper.getRealUpc().getAssociateUpcs().size();
			shipper.getRealUpc().getAssociateUpcs().forEach(associatedUpc -> {
				if(associatedUpc.getSellingUnit() != null) {
					associatedUpc.getSellingUnit().getUpc();
				}
			});
		}
	}
}