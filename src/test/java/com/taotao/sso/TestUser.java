package com.taotao.sso;

import org.junit.Test;

public class TestUser {

	@Test
	public void  run(){
		
		int a=1;
		int b=4;
		if(a!=1&&a!=2&&a!=3){
			System.out.println(" a jin && ");
		}
		if(b!=1&&b!=2&&b!=3){
			System.out.println("b jin &&");
		}
		
		if(a!=1||a!=2||a!=3){
			System.out.println(" a jin || ");
		}
		if(b!=1||b!=2||b!=3){
			System.out.println("b jin ||");
		}
		
	
		
	}
}
