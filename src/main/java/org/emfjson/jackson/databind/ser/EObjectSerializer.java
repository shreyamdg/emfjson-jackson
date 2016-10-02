/*
 * Copyright (c) 2015 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Guillaume Hillairet - initial API and implementation
 *
 */
package org.emfjson.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.eclipse.emf.ecore.EObject;
import org.emfjson.jackson.databind.property.EObjectProperty;
import org.emfjson.jackson.databind.property.EObjectPropertyMap;
import org.emfjson.jackson.utils.EObjects;

import java.io.IOException;

import static org.emfjson.jackson.databind.EMFContext.getParent;

public class EObjectSerializer extends JsonSerializer<EObject> {

	private final JsonSerializer<EObject> _refSer;
	private final EObjectPropertyMap.Builder builder;

	public EObjectSerializer(EObjectPropertyMap.Builder builder, JsonSerializer<EObject> serializer) {
		this.builder = builder;
		this._refSer = serializer;
	}

	@Override
	public Class<EObject> handledType() {
		return EObject.class;
	}

	@Override
	public void serialize(EObject object, JsonGenerator jg, SerializerProvider provider) throws IOException {
		EObjectPropertyMap properties = builder.construct(object.eClass());

		final EObject parent = getParent(provider);
		if (parent != null && (object.eIsProxy() || EObjects.isContainmentProxy(parent, object))) {
			// containment proxies are serialized as references
			_refSer.serialize(object, jg, provider);
			return;
		}

		jg.writeStartObject();
		for (EObjectProperty property : properties.getProperties()) {
			property.serialize(object, jg, provider);
		}
		jg.writeEndObject();
	}

//	@Override
//	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
//		return this;
//	}
}
