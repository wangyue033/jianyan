/**
 * 
 */
package com.dhcc.login.impl;

import java.io.Serializable;

import framework.dhcc.utils.BaseBean;

/**
 * @author zhurx
 * 
 */
public class Popedom extends BaseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3225964424252853389L;
	private String popedomId;
	private String popedomName;
	private String funcId;
	private String description;
	private String jspFunc;
	private String jspIcon;

	public Popedom() {
	}

	public Popedom(String popedomId, String popedomName, String funcId) {
		this.popedomId = popedomId;
		this.popedomName = popedomName;
		this.funcId = funcId;
	}

	public Popedom(String popedomId, String popedomName, String funcId,
			String jspFunc, String jspIcon) {
		this.popedomId = popedomId;
		this.popedomName = popedomName;
		this.funcId = funcId;
		this.jspFunc = jspFunc;
		this.jspIcon = jspIcon;
	}

	public String getPopedomId() {
		return popedomId;
	}

	public void setPopedomId(String popedomId) {
		this.popedomId = popedomId;
	}

	public String getPopedomName() {
		return popedomName;
	}

	public void setPopedomName(String popedomName) {
		this.popedomName = popedomName;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJspFunc() {
		return jspFunc;
	}

	public void setJspFunc(String jspFunc) {
		this.jspFunc = jspFunc;
	}

	public String getJspIcon() {
		return jspIcon;
	}

	public void setJspIcon(String jspIcon) {
		this.jspIcon = jspIcon;
	}
}
