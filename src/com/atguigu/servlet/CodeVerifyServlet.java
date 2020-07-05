package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;

@WebServlet("/CodeVerifyServlet")
public class CodeVerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeVerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//��ȡ��֤����ֻ���
		String phone_no = request.getParameter("phone_no");
		String verify_code = request.getParameter("verify_code");
		//ƴ��key
		String codeKey = "Verify_code:" + phone_no + ":code";
		//��redis�л�ȡ�ֻ�������Ӧ����֤��
		Jedis jedis = new Jedis("192.168.12.111", 6379);
		String code = jedis.get(codeKey);
		if(code.equals(verify_code)) {
			response.getWriter().print(true);
		}
		jedis.close();
		
	}

}

