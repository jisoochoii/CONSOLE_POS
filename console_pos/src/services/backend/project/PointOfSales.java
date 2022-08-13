package services.backend.project;

import java.util.ArrayList;

import beans.OrderInfo;
import beans.SalesInfo;
import database.project.DataAccess;
/*************이윤주***************/



public class PointOfSales {
	public PointOfSales() {
		
	}
	String orderCode;
	
	//제어
	public String backController(String clientData) {
		String serverData = null;
		String message= null;
		String jobCode = clientData.substring(0, clientData.indexOf("?"));
		
		
		   if(clientData.equals("GS")) {
			   message =this.getStoreInfoCtl(clientData.substring(clientData.indexOf("?")+1));
			   }
		   else if(clientData.equals("GS")&&clientData.contains("storeCode")) {
			   message = this.getGoodsInfoCtl(clientData.substring(clientData.indexOf("?")+1));
		   }
		   else if(clientData.equals("PA")&&clientData.contains("quantity")) {
			   message = this.paymentCtl(clientData.substring(clientData.indexOf("?")+1));
		   }
		   else if(clientData.equals("PA")&&clientData.contains("point")) {
			   message = this.paymentCtl(clientData.substring(clientData.indexOf("?")+1));
		   	}
		return message;
		   }
		
		
		/*
		switch(jobCode) {
		case "GetStoreInfo":
			serverData = this.getStoreInfoCtl(clientData.substring(clientData.indexOf("?")+1));
			break;
			
		case "GetGoodsInfo":
			serverData = this.getGoodsInfoCtl(clientData.substring(clientData.indexOf("?")+1));
			break;
			
		case "Payment" :
			serverData =this.paymentCtl(clientData.substring(clientData.indexOf("?")+1));
			break;
			
		case "Order" :
			serverData = this.orderCtl(clientData.substring(clientData.indexOf("?")+1));
			
		default:
		}
		return serverData;
	}
	*/
	//포인트정보저장
	private String orderCtl(String clientData) { 
		String[] pointInfo =null;
		//String pointCode = null;
		OrderInfo oi = new OrderInfo();
		DataAccess dao = new DataAccess();
		boolean Info;
		StringBuffer sb = new StringBuffer();
		String msg = new String();
		//ArrayList<OrderInfo> orderList = new ArrayList<OrderInfo>();
		
		/*주문코드 (OrderDate?orderCode= yyyyMMddHHmmssstorecode     &employeeCode=E01   &point=243141*/
		pointInfo= clientData.split("&");
		//pointCode = pointInfo[2].substring(pointInfo[0].indexOf("=")+1);
		
		if(dao.fileConnection(false, "J:\\smartweb\\youn\\pos\\src\\database\\orders.txt", true)) {
			for(int idx=0; idx<pointInfo.length; idx++) {
				sb.append(pointInfo[idx]);
				if(idx<pointInfo.length-1) {
					sb.append(",");
				}
			}
			Info = dao.ins(sb.toString());
			msg ="성공";
		}
		
		dao.fileClose();
		
		
		return msg;
			}
	
	
	//결제정보 저장
	private String paymentCtl(String clientData) {
		String[] order = null;
		String orderCode = null;
		ArrayList<OrderInfo> orderList = new ArrayList<OrderInfo>();
		DataAccess dao = new DataAccess();
		//SalesInfo si = new SalesInfo();
	
		/*데이터 추출*/
		
		order= clientData.split("&");
		orderCode = order[0].substring(order[0].indexOf("=")+1);
		for(int idx=1;idx<order.length; idx+=2) {
			OrderInfo oi = new OrderInfo(); 
			//orderCode = si.getDate()+ si.getStoreCode();
			oi.setGoodsCode(order[idx].substring(order[idx].indexOf("=")+1));
			oi.setQuantity(Integer.parseInt(order[idx+1].substring(order[idx+1].indexOf("=")+1)));
			orderList.add(oi);
		}
		/*DAO*/
		
		//재호출 true- 결제완료 false- 결제실패
		dao.fileConnection(false, "J:\\smartweb\\youn\\pos\\src\\database\\orders.txt", true); //쓰기, 새로추가
		
		return dao.setOrder(orderList)?"message=결제성공":"message=결제실패";
	}
	
	//매장이름확인
	private String getStoreInfoCtl(String clientData) {
			
		DataAccess dao=null;
		String returnValue = null;
		String storeName=null, message="매장없음";
		/*storeCode=XXX*/
		String storeCode =clientData.substring(clientData.indexOf("=")+1);
		
		/*dao*/
		dao =new DataAccess();
		dao.fileConnection(true,"J:\\smartweb\\youn\\pos\\src\\database\\storeTable.txt",false); 
		
		/*storeName 요청*/
		storeName = dao.getStoreName(storeCode);
		dao.fileClose();
		if(storeName !=null) {
			message="상품판매";
			returnValue = "storeName="+storeName+"&message=" + message;
		}
		
		return returnValue;
	}
	
	//db상품정보 불러오기
	private String getGoodsInfoCtl(String clientData) {
		
			DataAccess dao = null;
			String serverData = null;
			String[] goodsInfo = null;
			String message="상품없음";
		
			
			String[] info = clientData.split("&");
			this.extractData(info);
			
			dao = new DataAccess();
			dao.fileConnection(true, "J:\\smartweb\\youn\\pos\\src\\database\\goodsTable.txt", false);
			
			/* goodsInfo 요청 */
			goodsInfo = dao.salesGoodsInfo(info);
			dao.fileClose();
			
			/* 3. client로 보낼 serverData 생성*/
			if(goodsInfo != null) {
			
				message = goodsInfo[2].equals("P")? "성인전용:":"";
				message += goodsInfo[3].equals("Y")? "비과세:":"";
				
				
				serverData = "goodsName="+ goodsInfo[0] + "&goodsPrice=" + goodsInfo[1] + "&message=" + message; 
			}
			
			return serverData;
	}
	
	//해당인덱스의 사용자입력값만 추출
	private void extractData(String[] info) {
		for(int idx=0; idx<info.length; idx++ ) {
			info[idx] = info[idx].substring(info[idx].indexOf("=")+1);
			}
		}
	}


