package org.sabot.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

/*
 * Library Sabot: a library for accelerating GWT and AppEngine development
 * 
 * Copyright (C) 2011  Phil Craven, Stephen McLaughry
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

@Singleton
public class AnchorForwardingServlet extends HttpServlet {
	private static final long serialVersionUID = -9059598057474267777L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doIt(req, res);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doIt(req, res);
	}
	
	private void doIt(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String link = buildLink(req);
		res.sendRedirect(link);
	}

	private String buildLink(HttpServletRequest req) {
		String path = (null == req.getParameter("p")) ? "" : req.getParameter("p");
		String anchor = (null == req.getParameter("a")) ? "" : "#" + req.getParameter("a");
		return "/" + path + anchor;
	}
}