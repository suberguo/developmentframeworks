package com.dds.ssjh.usermodule;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;

import com.dds.ssjh.util.BeanUtil;

abstract class AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	protected final void init() {
		BeanUtil.inject(this);
	}

	protected void addCallbackParams(String name, Object value) {
		PrimeFaces.current().ajax().addCallbackParam(name, value);
	}

}
