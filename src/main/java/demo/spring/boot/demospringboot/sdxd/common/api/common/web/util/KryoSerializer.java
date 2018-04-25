package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigDecimalSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyMapSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptySetSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonMapSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonSetSerializer;
import de.javakaffee.kryoserializers.CopyForIterateCollectionSerializer;
import de.javakaffee.kryoserializers.CopyForIterateMapSerializer;
import de.javakaffee.kryoserializers.DateSerializer;
import de.javakaffee.kryoserializers.EnumMapSerializer;
import de.javakaffee.kryoserializers.EnumSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Map;

public class KryoSerializer {

    private static final Logger log = LoggerFactory.getLogger(KryoSerializer.class);

    private static final ThreadLocal<Kryo> KRYOS = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new KryoReflectionFactorySupport() {
                @Override
                public Serializer<?> getDefaultSerializer(final Class type) {
                    if (EnumSet.class.isAssignableFrom(type)) {
                        return new EnumSetSerializer();
                    }
                    if (EnumMap.class.isAssignableFrom(type)) {
                        return new EnumMapSerializer();
                    }
                    if (Collection.class.isAssignableFrom(type)) {
                        return new CopyForIterateCollectionSerializer();
                    }
                    if (Map.class.isAssignableFrom(type)) {
                        return new CopyForIterateMapSerializer();
                    }
                    if (Date.class.isAssignableFrom(type)) {
                        //noinspection unchecked
                        return new DateSerializer(type);
                    }
                    return super.getDefaultSerializer(type);
                }
            };
            kryo.setRegistrationRequired(false);
            //noinspection ArraysAsListWithZeroOrOneArgument
            kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
            kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
            kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
            kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
            kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
            kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
            kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
            kryo.register(BigDecimal.class, new BigDecimalSerializer());
            kryo.register(BigInteger.class, new BigIntegerSerializer());
            kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
            kryo.register(InvocationHandler.class, new JdkProxySerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);

            return kryo;
        }
    };

    private KryoSerializer() {}

    public static byte[] write(Object obj) {
        return write(obj, -1);
    }

    public static byte[] write(Object obj, int maxBufferSize) {
        Kryo kryo = KRYOS.get();
        Output output = new Output(1024 * 4, maxBufferSize);
        kryo.writeClassAndObject(output, obj);
        return output.toBytes();
    }

    public static Object read(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Kryo kryo = KRYOS.get();
        Input input = new Input(bytes);
        return kryo.readClassAndObject(input);
    }

    public static byte[] writeString(String str) {
        byte[] data = null;
        try {
            data = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("UTF-8 not supported, {}", e.getMessage());
        }
        return data;
    }

    public static String readAsString(byte[] bytes) {
        if (bytes == null)
            return null;
        String tempStr = null;
        try {
            tempStr = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("UTF-8 not supported, {}", e.getMessage());
        }
        return tempStr;
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String testStr = "test_str";
//        byte[] dataStr = KryoSerializer.writeToByte(testStr);
//        System.out.println(dataStr.toString());
//
//        String str = (String) KryoSerializer.readToStr(dataStr);
//        System.out.println(str);
//
//    }
}
