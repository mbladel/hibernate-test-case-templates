package org.hibernate.bugs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

//	private static final XmlMapper objectMapper = new XmlMapper();
//	private static final ObjectMapper objectMapper = new ObjectMapper();

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();


//		final XmlMapper objectMapper = XmlMapper.builder().configure( ToXmlGenerator.Feature.WRITE_NULLS_AS_XSI_NIL, true ).build();

		//instantiate a factory without namespace support
//		final XMLInputFactory inputF = XMLInputFactory.newFactory();
//		inputF.setProperty( XMLInputFactory.IS_NAMESPACE_AWARE, false );

//		final XmlMapper objectMapper = new XmlMapper( /*inputF*/ );
		final XmlMapper objectMapper = XmlMapper.builder()
				.defaultUseWrapper( false )
				// enable/disable Features, change AnnotationIntrospector
				.build();
		objectMapper.findAndRegisterModules();
		objectMapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
		objectMapper.enable( ToXmlGenerator.Feature.WRITE_NULLS_AS_XSI_NIL );
//		objectMapper.enable( FromXmlParser.Feature.PROCESS_XSI_NIL );
//		objectMapper.enable( FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL );


		final SimpleModule module = new SimpleModule();
//		module.addSerializer( String.class, new CustomStringSerializer() );
		module.addDeserializer( String.class, new NullableStringDeserializer() );
//		module.addSerializer( String[].class, new StringArraySerializer() );
//		module.addDeserializer( String[].class, new StringArrayDeserializer() );
		objectMapper.registerModule( module );

//		LocalDate[] array = new LocalDate[] { LocalDate.now() };

//		System.out.println( objectMapper.writeValueAsString( array ) );
//
//		System.out.println( objectMapper.writeValueAsString( LocalDate.now() ) );
//

		final String[] originalArray = new String[] { "", "test", null, "test2" };
		final String arrayString = objectMapper.writeValueAsString( originalArray );
		System.out.println( arrayString );

		final String[] result = objectMapper.readValue( arrayString, String[].class );


//		final String[] result2 = objectMapper.readValue( "<Strings></Strings>", String[].class );

//		final String[] nullArray = null;
//		final String[] result3 = objectMapper.readValue( objectMapper.writeValueAsString( nullArray ), String[].class );

		System.out.println( Arrays.toString( result ) );

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable( ToXmlGenerator.Feature.WRITE_NULLS_AS_XSI_NIL );
		xmlMapper.enable( FromXmlParser.Feature.PROCESS_XSI_NIL );

		String string = xmlMapper.writeValueAsString( new String[] { "", "test", null, "test2" } );
		// string: <Strings><item></item><item>test</item><item xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/><item>test2</item></Strings>
		String[] parsed =  xmlMapper.readValue( string, String[].class );
		// parsed: ["", "test", "", "test2"]


		final Long[] originalLongArray = new Long[] { 1L, 2L, null, 3L };
		final String longString = objectMapper.writeValueAsString( originalLongArray );
		final Long[] longResult = objectMapper.readValue( longString, Long[].class );

//		final String[] result2 = objectMapper.readValue( "<Strings><item></item><item>test</item><item xsi:nil=\"true\"/><item>test2</item></Strings>", String[].class );

//		System.out.println( objectMapper.writeValueAsString( new StringWrapper[] { new StringWrapper( "" ), new StringWrapper( "test" ), new StringWrapper( null ), new StringWrapper( "test2" ) } ) );

//		System.out.println( objectMapper.writeValueAsString( new MyEnum[] {
//				MyEnum.FALSE,
//				MyEnum.FALSE,
//				null,
//				MyEnum.TRUE
//		} ) );


//		LocalDateTime test1 = LocalDateTime.now();
//		System.out.println( objectMapper.writeValueAsString( test1 ) );

//		System.out.println( "### JAXB ###" );
//
//		JAXBContext context = JAXBContext.newInstance( String[].class );
//		Marshaller m = context.createMarshaller();
//		m.marshal( originalArray, System.out );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@JsonInclude( JsonInclude.Include.NON_NULL )
	private static class StringWrapper {
		@JsonRawValue
		private final String value;

		@JsonCreator
		public StringWrapper(@JsonProperty( "value" ) String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum MyEnum {
		FALSE, TRUE
	}

//	private static class StringSerializer extends JsonSerializer<String> {
//		@Override
//		public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//			if ( value == null ) {
//				jgen.writeNull();
//			}
//			else {
//				jgen.writeString( value );
//			}
//		}
//	}

	private static class StringArraySerializer extends JsonSerializer<String[]> {
		@Override
		public void serialize(String[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			// todo marco : how ??
			jgen.writeNull();
		}
	}

	private static class SimpleClass {
		private String[] values;

		public SimpleClass(String[] values) {
			this.values = values;
		}

		public String[] getValues() {
			return values;
		}

		public void setValues(String[] values) {
			this.values = values;
		}
	}

	private static class NullableStringDeserializer extends JsonDeserializer<String> {
		private static final StringDeserializer delegate = new StringDeserializer();

		@Override
		public String deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException {
			JsonToken token = jp.nextToken();
			return "";
		}
	}

	private static class StringArrayDeserializer extends JsonDeserializer<String[]> {
		@Override
		public String[] deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
			final ArrayList<String> result = new ArrayList<>();
			JsonToken token;
			while ( ( token = jp.nextValue() ) != JsonToken.END_OBJECT ) {
				if ( token.isScalarValue() ) {
					result.add( jp.getValueAsString() );
				}
			}
			return result.toArray( String[]::new );
		}
	}

	private static class StringCollectionDeserializer extends JsonDeserializer<Collection<String>> {
		@Override
		public Collection<String> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
			final ArrayList<String> result = new ArrayList<>();
			JsonToken token;
			while ( ( token = jp.nextValue() ) != JsonToken.END_OBJECT ) {
				if ( token.isScalarValue() ) {
					result.add( jp.getValueAsString() );
				}
			}
			return result;
		}
	}

	class NullableItemsDeserializationProblemHandler extends DeserializationProblemHandler {
		@Override
		public Object handleUnexpectedToken(
				DeserializationContext ctxt,
				Class<?> targetType,
				JsonToken t,
				JsonParser p,
				String failureMsg) throws IOException {
			if ( targetType == Long.class && p.currentToken() == JsonToken.START_OBJECT ) {
				boolean isNull = false;
				while ( p.currentToken() != JsonToken.END_OBJECT ) {
					p.nextToken();
					switch ( p.currentToken() ) {
						case FIELD_NAME:
							if ( "nil".equals( p.getText() ) ) {
								isNull = true;
							}
					}
				}
				if ( isNull ) {
					return null;
				}
			}
			return super.handleUnexpectedToken( ctxt, targetType, t, p, failureMsg );
		}
	}
}
