package com.midterm.group4.utils;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.html2pdf.HtmlConverter;
import com.midterm.group4.data.model.Invoice;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.ByteArrayOutputStream;

@Component
public class DocumentUtils {

    private final SpringTemplateEngine templateEngine;

    public DocumentUtils(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateByteInvoice(Invoice invoice) throws IOException {
        Context context = new Context();

        LocalDate currentDate = LocalDate.now();

        context.setVariable("listOrderItem", invoice.getListOrderItem());
        context.setVariable("invoice", invoice);
        context.setVariable("currentDate", currentDate);

        String processedHtml = templateEngine.process("data/index", context);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(processedHtml, stream);
        stream.flush();
        return stream.toByteArray();
    }    
}