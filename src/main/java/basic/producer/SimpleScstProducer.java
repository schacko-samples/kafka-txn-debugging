package basic.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SimpleScstProducer {

    @Autowired
    StreamBridge streamBridge;

    @Autowired Foo foo;

    public static void main(String[] args) {
        SpringApplication.run(SimpleScstProducer.class, args);
    }

    @PostMapping("/send-data")
    public void passthrough() throws InterruptedException {

        foo.foo(streamBridge);
    }

    @Component
    static class Foo {


        @Transactional //("customKafkaTransactionManager")
        public void foo(StreamBridge streamBridge) {
            for (int i = 0; i < 5; i++) {
                streamBridge.send("mySupplier-out-0", "foobar " + i);
            }

        }

//        @Bean
//        KafkaTransactionManager customKafkaTransactionManager() {
//            KafkaMessageChannelBinder kafka = (KafkaMessageChannelBinder) this.binderFactory.getBinder("kafka", MessageChannel.class);
//            ProducerFactory<byte[], byte[]> transactionalProducerFactory = kafka.getTransactionalProducerFactory();
//            KafkaTransactionManager kafkaTransactionManager = new KafkaTransactionManager(transactionalProducerFactory);
//            return kafkaTransactionManager;
//        }


    }
}
