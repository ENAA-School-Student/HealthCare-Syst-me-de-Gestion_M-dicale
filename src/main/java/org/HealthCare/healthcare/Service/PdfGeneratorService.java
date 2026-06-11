package org.HealthCare.healthcare.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGeneratorService {

    public byte[] generateDossierMedicalPdf(DossierMedical dossier) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Dossier Médical", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Spacer

        Patient patient = dossier.getPatient();
        if (patient != null) {
            document.add(new Paragraph("Patient: " + patient.getNom() + " " + patient.getPrenom()));
            document.add(new Paragraph("Téléphone: " + patient.getTelephone()));
            document.add(new Paragraph("Date de naissance: " + patient.getDateNaissance()));
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Date de création: " + dossier.getDateCreation()));
        document.add(new Paragraph("Diagnostic:"));
        document.add(new Paragraph(dossier.getDiagnostic()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Observation:"));
        document.add(new Paragraph(dossier.getObservation()));

        document.close();
        return out.toByteArray();
    }

    public byte[] generateRendezVousListPdf(Patient patient, List<RendezVous> rendezVousList) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Liste des Rendez-vous", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Patient: " + patient.getNom() + " " + patient.getPrenom()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell("Date");
        table.addCell("Médecin");
        table.addCell("Statut");

        for (RendezVous rdv : rendezVousList) {
            table.addCell(rdv.getDateRendezVous().toString());
            table.addCell(rdv.getMedecin() != null ? rdv.getMedecin().getNom() : "N/A");
            table.addCell(rdv.getStatut().toString());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public byte[] generateSimpleReportPdf(String content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Rapport Médical", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(content));

        document.close();
        return out.toByteArray();
    }
}
