package sales.frontend.project;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
import beans.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import backend.project.*;




public class Sales {
	Scanner scanner;
	
	private int amount=0;
	public Sales(Scanner scanner) {
		this.scanner = scanner;
		this.entrance();
		
	}
	
	/* JOB : 상품판매에 따른 기능 구현(결제시 총금액의 10% 포인트 적립)  
	 *   Goods.txt : 상품조회(GS), 결제(PA)
	 *   Orders.txt : 결제(PA)
	 *   Points.txt : 결제(PA)
	 * */
	private void entrance() {
		SalesInfo si = new SalesInfo();
		Controller ctl = new Controller();
		String[] serverData = null;
		OrderInfo oi = new OrderInfo();
		ArrayList<OrderInfo> orderList = null;
		String ui = null;
		int quantity = 0;
		int pay = 0;
		String clientData = null;
		String storeCode = null;
		String clientData1 = null;
		
		
		String[] menu = {"결제","수량변경","상품추가","보류"};
		/* 판매업무 */
		while(true) {
		this.display(this.title("매장코드"));
		si.setStoreCode(this.userInput());
		//서버       //이름을 가져와도 넣을수가없어서 
		serverData=ctl.entrance(this.makeTransferData("GS", "storeCode", si.getStoreCode())).split("&");
		if(serverData != null) {
			si.setStorename(serverData[0].substring(serverData[0].indexOf("=")+1));
			break;
		}else {continue;}
		}
		orderList = new ArrayList<OrderInfo>();
		
		while(true) {
		this.display(this.title("상품코드"));
		si.setGoodsCode(this.userInput());
		
		serverData=ctl.entrance(this.makeTransferData(this.makeTransferData("GS","storeCode",si.getGoodsCode()),orderList)).split("&");
		orderList.get(orderList.size()-1).setGoodsName(serverData[0].substring(serverData[0].indexOf("=")+1));
		orderList.get(orderList.size()-1).setGoodsPrice(Integer.parseInt(serverData[1].substring(serverData[1].indexOf("=")+1)));
		orderList.get(orderList.size()-1).setQuantity(1);
		si.setMessage(serverData[2].substring(serverData[2].indexOf("=")+1));
		
		this.display(this.makeOrderList(orderList)); // 선택한상품 기본값1해서 출력
		
		while (true) {
			this.display(this.makeSubMenu(menu));
			ui = this.userInput();

			if (ui.equals("1")) {
				this.display("[" + menu[Integer.parseInt(ui)] + "]");
				quantity = Integer.parseInt(this.userInput());
				orderList.get(orderList.size() - 1).setQuantity(quantity);
			} else if (!ui.equals("1"))
			{break;}
			
			this.display(this.title(menu[Integer.parseInt(ui)]));
			this.display(this.makeOrderList(orderList));
			}
			if(ui.equals("0")) {
				break;
			}
		
		}
		
		while(true) {
			this.display("========== 받을 돈 : "+this.amount +"\n");
			this.display("적립예정포인트 : ");
			this.display(Double.parseDouble(String.format("%.2f%n",amount*10.0/100.0))+"\n");
			this.display("---------- 받은 돈 :");
			pay = Integer.parseInt(this.userInput());
			this.display("---------- 거스름 돈 :"+ (pay-this.amount+"\n"));
			this.display("========== 결제 완료 :");
			if(this.userInput().equals("Y")) {
				
				clientData = this.paymentData(si, orderList);
				serverData = ctl.entrance(clientData).split("&");
				
				oi.setPoint(Double.parseDouble(String.format("%.2f%n",amount*10.0/100.0)));
				
				clientData =this.orderData(si.getStoreCode(), oi, si);
				serverData = ctl.entrance(clientData1).split("&");
			}
			if(serverData[0].contains("성공")){break;}
			
		}

	}


	
	//입력받은 정보를 서버전송양식에맞게 문자열로 변환 포인트부분
	private String orderData(String storecode,OrderInfo oi,SalesInfo si) {
		/*주문코드 (OrderDate?orderCode=yyyyMMddHHmmssstorecode&point=243141*/
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMddHHmmss");
		String clientData = "PA?orderCode="+now.format(date)+storecode+"&employees"+oi.getEmployeesCode()+"&point="+oi.getPoint();
		return clientData;
	}
	
	//입력받은 정보를 서버전송양식에맞게 문자열로 변환 결제부분
	private String paymentData(SalesInfo si, ArrayList<OrderInfo> orderList) {
		/*주문코드 (yyyyMMddHHmmss+storeCode),상품코드,수량*/
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMddHHmmss");
		String a = null, b = null;
		a = now.format(date);
		si.setDate(a);
		String clientData = "PA?orderCode="+ a + si.getStoreCode();
		for(OrderInfo oi: orderList) {
			clientData += "&"+"goodsCode="+oi.getGoodsCode()+"&quantity="+oi.getQuantity();
		}
		
		return clientData;
	}
	
	/* 주문리스트 */
	private String makeOrderList(ArrayList<OrderInfo> orderList) {
		StringBuffer list = new StringBuffer();
		DecimalFormat formatter = new DecimalFormat("###,###");
		
		list.append(" --------------------------------------------------\n");
		list.append("  상품코드    상품명         판매가   수량       합계\n");
		list.append(" --------------------------------------------------\n");
		for(OrderInfo oi:orderList) {
			list.append("  " + oi.getGoodsCode());
			list.append("  " + ((oi.getGoodsName().length()>5)? oi.getGoodsName().substring(0, 5): oi.getGoodsName()));
			//5글자 이상 안찍히게.					0~부터5바로앞까지 01234 5글자
			list.append("  " + formatter.format(oi.getGoodsPrice()));
			list.append("  " + oi.getQuantity());
			list.append("  " + formatter.format(oi.getGoodsPrice() * oi.getQuantity())); //합계
			list.append("\n");
			amount += oi.getGoodsPrice() * oi.getQuantity(); //소계
			list.append("\n");
		}


		list.append(" --------------------------------------------------\n");
		list.append("    소  계                       "+ formatter.format(amount) +"\n");
		return list.toString();
	}
	
	//메뉴출력
	private String makeSubMenu(String[] menu) {
		StringBuffer list = new StringBuffer();
		list.append(" [Select]__________________________________________\n\n");
		for(int idx=0; idx<menu.length; idx++) {
			list.append("  " + idx + ". " + menu[idx] + "   ");
		}
		list.append("\n");
		list.append(" _______________________________________ Select : ");
		return list.toString();
	}
	
	//타이틀화면
	private String title(String subject) {
		StringBuffer title = new StringBuffer();
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분ss초 E요일");
		now.format(date);

		title.append("\n\n\n");
		title.append("┏━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		title.append("  ■■■■         ■■         ■■■ \n");
		title.append("  ■    ■     ■    ■      ■         \n ");
		title.append("  ■■■■      ■      ■      ■■■       \n ");
		title.append("  ■          ■    ■          ■    \n Designed by ♥2조♥  ");
		title.append("  ■            ■■        ■■■\n");
		title.append("  "+now.format(date)+ "\n");
		title.append("┗━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━━━━━━┛\n\n");
		title.append("[ "+subject+" ]");

		return title.toString();
	}
	
	private String userInput() {
		return this.scanner.next();
	}
	private String userInput(String item) {
		System.out.println("[" + item + "]");
		return this.scanner.next();
	}
	private void display(String contents) {
		System.out.println(contents);
	}
	
	//입력받은 정보를 서버전송양식에맞게 문자열로 변환
	private String makeTransferData(String jobCode, String dataName,String data) {		
		return jobCode +"?"+ dataName+"="+data;
	}
	
	//입력받은 정보를 서버전송양식에맞게 문자열로 변환 //굿즈코드.수량 등등 받을때
	private String makeTransferData(String jobInfo, ArrayList<OrderInfo> orderList) {
		StringBuffer sb = new StringBuffer();
		sb.append(jobInfo);
		sb.append("&goodsCode="+ orderList.get(orderList.size()-1).getGoodsCode());
		//}else { //서버전송용
		//for(int idx=0;idx<orderList.size(); idx++) {
		//sb.append("&goodsCode="+ orderList.get(idx).getGoodsCode());
		//sb.append("&quantity="+ orderList.get(idx).getQuantity());
		//}
		//}
		return sb.toString();
	}
}
