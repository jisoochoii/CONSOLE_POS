package mgr.frontend.project;
/*************임현***************/
import java.util.Scanner;

import backend.project.Controller;

public class Managements {
	Scanner scanner;
	
	public Managements(Scanner scanner) {
		this.scanner = scanner;
		this.entrance();
	}
	
	/* JOB - Managements
	 *   1. Store.txt : 매장명 수정(ST)
	 *   2. Employees.txt : 직원등록(EI), 직원비번수정(EU)
	 *   3. Members.txt : 회원등록(MI)
	 * */
	
	/* 매장명수정, 회원등록, 직원등록, 직원비번수정 업무 선택 */
	//유저입력값에 따라서 업무선택
	private void entrance() {
		String ui = new String();
		while(true) {
			this.display(this.title("메인"));
			this.display("[ 메뉴 선택 ]━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━\n");
			this.display(" 1. 매장명 수정  	2. 회원등록      3. 직원관리       0. 이전화면\n");
			this.display("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━ 선택 : ");
			ui = this.userInput();
			//유효성 검사는 수행 메서드에서 함

			if(ui.equals("1")) {  //매장명 수정 이동
				this.storeManagementCtl();
			}
			else if(ui.equals("2")) {  //회원등록 이동
				this.memberManagementCtl();
			}
			else if(ui.equals("3")) {
			this.employeeManagementCtl();
			}
			else if(ui.equals("0")) { //프로그램 종료
				break;
			}
			else {

			}
		}
	}
	
	// 매장 수정업무 storemanagement jobcode달아서 보내주기 ST
	private void storeManagementCtl() {
		Controller ctl = null;
		String[] storeInfo = {"매장코드", "매장이름"};
		String[] info= new String[2];
		String storeCode = new String();
		String storeName = new String();
		String jobCode = "ST";
		int num=0;
		
		//매장 입력
		this.display(this.title("매장 이름 수정"));
		while(true) {
			this.display("매장코드 입력 : ");
			storeCode = this.userInput();

			//수정 데이터 입력
			this.display("변경할 매장명 입력 : ");
			storeName = this.userInput();
			
			info[num] = storeName;

			
			if(storeInfo != null) {
				ctl = new Controller();
				//jobCode=xx?storeCode=xxx&storename=xxx
				this.display(ctl.entrance(this.makeTransferData(jobCode, storeInfo, info)));
				this.display(this.makeTransferData(jobCode, storeInfo, info));
			}
		}
	}
	
	//회원등록업무 MemberManagements MI
	private void memberManagementCtl() {
		
		Controller ctl = null;
		String ui = new String();
		String[] memberInfo = new String[2];
		String[] memberList = {"회원코드","회원이름"};
		String clientData = new String();
		String jobCode = "MI";
		this.display(this.title("회원 등록"));

		//회원정보 입력부분
		for(int colIdx=0; colIdx<memberList.length; colIdx++) {
			this.display(memberList[colIdx] + " : ");
			memberInfo[colIdx] = this.userInput();
		}
		
		if(memberInfo != null) {
			ctl = new Controller();
			this.display(ctl.entrance(this.makeTransferData(jobCode,memberList, memberInfo)));
			this.display(this.makeTransferData(jobCode,memberList, memberInfo));
		}
	}

	
	//직원등록,직원비번수정 업무 Storemanagement EI,EU
	private void employeeManagementCtl() {
		/* 직원관리 제어*/
		Controller ctl = null;
		String ui = new String();
		String[] memberInfo = null;
		String[][] Items = {{"매장코드","직원코드", "비밀번호", "직원이름"},{"매장코드","직원코드", "비밀번호", "직원이름"}};
		String clientData = new String();
		String jobCode = null;
		while(true) {
			this.display(this.title("직원관리"));
			this.display("[ 메뉴 선택 ]━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━");
			this.display("   1. 직원등록            2. 비밀번호 수정            0. 이전화면");
			this.display("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━ 선택 :");
			
			ui = this.userInput();
			if(ui.equals("1")) { //직원등록으로 이동
				jobCode = new String();
				jobCode = "EI";
				memberInfo =	this.regEmployee(Items[Integer.parseInt(ui)-1]);
			}else if(ui.equals("2")){ //비밀번호수정으로 이동
				jobCode = new String();
				jobCode = "EU";
				memberInfo = this.modEmployee(Items[Integer.parseInt(ui)-1]);
			}else if(ui.equals("0")){  //이전화면으로 이동 
				break;
			}
			if(memberInfo != null) {
				ctl = new Controller();
				this.display(this.makeTransferData(jobCode, Items[Integer.parseInt(ui)-1], memberInfo));
				this.display(ctl.entrance(this.makeTransferData(jobCode, Items[Integer.parseInt(ui)-1], memberInfo)));
			}
		}
	}
	
	//직원등록업무
	private String[] regEmployee(String[] employee) {
		String[] employeeInfo = new String[4];
		this.display(this.title("회원 등록"));
		//회원정보 입력부분
		for(int colIdx=0; colIdx<employee.length; colIdx++) {
			this.display(employee[colIdx] + " : ");
			employeeInfo[colIdx] = this.userInput();
		}
		return employeeInfo;
	}
	
	//직원비번수정업무
	private String[] modEmployee(String[] employee) {
		String[] employeeInfo = new String[4];
		int sNum;

		//수정할 회원코드 입력
		this.display(this.title("비밀번호 수정"));
		for(int idx=0; idx<2; idx++) {
			this.display(employee[idx]+" : ");
			employeeInfo[idx] = this.userInput();
		}

		//수정할 항목 선택
		this.display("[ 메뉴 선택 ]━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━");
		this.display("   1. 비밀번호 수정            0. 이전화면");
		this.display("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━ 선택 :");
		sNum = Integer.parseInt(this.userInput())+1;

		//수정 데이터 입력
		this.display(employee[sNum] + " : ");
		employeeInfo[sNum] = this.userInput();

		return employeeInfo;
	}
	
	
	//타이틀화면
	private String title(String subject) {
		StringBuffer title = new StringBuffer();

		title.append("\n\n");
		title.append("┏━━━━━━━━━━━━━━━━━━━━━━━ʕ•㉨•ʔ━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		title.append("  ■■■■         ■■■        ■■■■\n");
		title.append("  ■       ■     ■      ■    ■\n");
		title.append("  ■■■■       ■       ■     ■■■\n ");
		title.append(" ■              ■      ■            ■ Designed by ♥2조♥\n");
		title.append("  ■                ■■■      ■■■■\n");
		title.append("  [ " + subject + " ] \n");
		title.append("┗━━━━━━━━━━━━━━━━━━━━━━━━━ ☆ ━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");

		return title.toString();
	}
	
	//메뉴출력
/*	private String makeMenu(String[] menu ) {

	} */

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
	
	//입력받은 정보를 양식에맞게 문자열로 변환 후 서버에 전송
	private String makeTransferData(String jobCode, String[] nameSheet, String[] data) {
		StringBuffer b = new StringBuffer();
		b.append(jobCode + "?");
		for(int idx=0; idx<data.length; idx++) {
			if(data[idx] != null) b.append(nameSheet[idx].substring(0,4) + "=" +  data[idx]);
			if(idx < data.length-1) if(data[idx+1] != null) b.append("&");
		}
		return b.toString();
	}
	
}
