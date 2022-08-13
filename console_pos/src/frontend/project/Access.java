package frontend.project;
/*************임현***************/
import java.util.ArrayList;
import java.util.Scanner;

import backend.project.Controller;
import beans.OrderInfo;
import beans.SalesInfo;
import mgr.frontend.project.*;
import sales.frontend.project.Sales;

public class Access {
	Scanner scanner;
	SalesInfo si = new SalesInfo();
	OrderInfo oi = new OrderInfo();
	
	public Access() {
		scanner = new Scanner(System.in);
		systemAccessCtl();
		scanner.close();
	}
	
	/* JOB - SA : System Access
	 *   1. Store.txt : 매장코드 유무 확인
	 *   2. Employees.txt : 매장코드 직원코드 패스워드 확인
	 *   3. DB정보와의 일치여부에 따라
	 *      일치   >> 관리단인 Managements Class나 판매단인 Sales Class로 이동 가능 
	 *      불일치 >>  재입력 또는 종료
	 * */
	private void systemAccessCtl() {
		//로그인 화면
		//매장코드 직원코드 비밀번호 입력받음
		//서버 연결해서 유효성검사
		//일치하면 상품판매or포스관리 선택메서드로 넘어가기
		//매장코드 직원코드 비번 내용쓰지말고 메서드 틀만 만들기,..,.,.,.,.,...무슨역ㅎ할인지 상단에 주석처리해서 써놓기
		Controller ctl = null; 
		String[] serverData = null;

		String clientData=null;
		String stCode;
		String emCode;
		String jobCode = "SA";
		String password = new String();
		while(true) {
			ctl = new Controller();
			
			this.display("매장코드 입력 : ");
			stCode = this.userInput();
			si.setStoreCode(stCode);
			
			this.display("직원코드 입력 : ");
			emCode = this.userInput();
			oi.setEmployeesCode(emCode);
			this.display("비밀번호 입력 : ");
			password = this.userInput();
			
			
		    clientData= jobCode+"?"+"매장코드"+"="+si.getStoreCode()+"&"+"직원코드"+"="+oi.getEmployeesCode()+"&"+"비밀번호"+"="+password;
			this.display(ctl.entrance(clientData));
			//if() {
			mainController();	
			//}
			
		}
		
		
	}
	
	//타이틀 화면
	private String title(String sub) {
		StringBuffer title = new StringBuffer();

		title.append("\n\n");
		title.append("┏━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		title.append("  ■■■■         ■■■        ■■■■\n");
		title.append("  ■       ■     ■      ■    ■\n");
		title.append("  ■■■■       ■       ■     ■■■\n ");
		title.append(" ■              ■      ■            ■ Designed by ♥2조♥\n");
		title.append("  ■                ■■■      ■■■■\n");
		title.append("  [ " + sub + " ] \n");
		title.append("┗━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");

		return title.toString();
	}
	
	//상품판매or포스관리 선택화면
	private void mainController() {
		String ui = new String();
		while(true) {
			this.display(this.title("메인"));
			this.display("[ 메뉴 선택 ]━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━\n");
			this.display(" 1. 상품판매  	2. POS 관리      0. 이전화면\n");
			this.display("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━ 선택 : ");
			ui = this.userInput();

			if(ui.equals("1")) {  //상품판매로 이동
				new Sales(scanner);
			}
			else if(ui.equals("2")) {  //pos관리로 이동
				new Managements(scanner);
			}
			else if(ui.equals("0")) { //이전화면
				break;
			}
			else {
				this.display("!-----0, 1, 2 중 선택-----!");
			}
		}
	}

	
	
	
	private String userInput() {
		return this.scanner.next();
	}
	private String userInput(String item) {
		//System.out.println("[" + item + "]");
		return this.scanner.next();
	}
	private void display(String contents) {
		System.out.print(contents);
	}
}
