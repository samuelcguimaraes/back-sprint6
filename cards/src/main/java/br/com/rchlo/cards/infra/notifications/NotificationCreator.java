package br.com.rchlo.cards.infra.notifications;

import br.com.rchlo.cards.domain.transactions.Transaction;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationCreator {

    private final Configuration freemarker;

    public NotificationCreator(Configuration freemarker) {
        this.freemarker = freemarker;
    }

    public String createFor(Transaction transaction) {
        try {
            Template template = freemarker.getTemplate("expense-notification.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("transaction", transaction);
            StringWriter out = new StringWriter();
            template.process(data, out);
            return out.toString();
        } catch (IOException | TemplateException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
