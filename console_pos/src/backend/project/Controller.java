package backend.project;

import sales.frontend.project.Sales;
import services.backend.project.*;
/*************김재필***************/
public class Controller {

	public String entrance(String clientData) {
		MemberManagements mm = null;
		StoreManagements sm = null;
		String jobCode = null;
		String message = null;
		Sales sales = null;
		PointOfSales PA = null;

		//modGoods?상품코드=1001&매입가격=1000
		jobCode = clientData.substring(0,clientData.indexOf("?"));

		switch(jobCode) {
		case "SA" :
		case "EI" :
		case "EU" :
		case "ST" :
			
			
			sm = new StoreManagements();
			message = sm.backController(clientData);
			break;
		case "GS": 
			message = PA.backController(clientData);
			break;
			
		case "PA": //order
			message = PA.backController(clientData);
			break;
		case "regMember": case "modMember" : case "MI" :
		/*MemberManagements*/
			mm = new MemberManagements();
		break;

		case "modStore" :
		/*StoreManagements*/
			sm = new StoreManagements();
		break;

		default:
		}
		return message;
	
	}
}
