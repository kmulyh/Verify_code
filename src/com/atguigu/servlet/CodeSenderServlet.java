package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;


@WebServlet("/CodeSenderServlet")
public class CodeSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��ȡ�ֻ���
		String phone_no = request.getParameter("phone_no");
		//��ȡ��֤��
		String code = getCode(6);
		//ƴ��key
		String codeKey = "Verify_code:" + phone_no + ":code";//Verify_code:12345:code
		String countKey = "Verify_code:" + phone_no + ":count";
		
		Jedis jedis = new Jedis("192.168.12.111", 6379);
		//�жϷ�����֤��Ĵ���
		String count = jedis.get(countKey);
		if(count == null) {
			//�����һ��
			jedis.setex(countKey, 24*60*60, "1");
		}else if(Integer.parseInt(count) <= 2) {
			jedis.incr(countKey);
		}else if(Integer.parseInt(count) > 2) {
			response.getWriter().print("limit");
			jedis.close();
			return ;
		}
		
		//��redis�н��д洢�����ֻ���Ϊ��������֤��Ϊֵ
		jedis.setex(codeKey, 120, code);
		jedis.close();
		response.getWriter().print(true);
		
	}
	
	
	private String getCode(int length) {
		String code = "";
		Random random = new Random();
		for(int i = 0; i < length; i++) {
			int rand = random.nextInt(10);
			code += rand;
		}
		return code;
	}

}