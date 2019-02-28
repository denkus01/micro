package com.clrk.lambda.trydynamodb.importer;

import com.clrk.lambda.trydynamodb.dao.ErrorLogDao;
import com.clrk.lambda.trydynamodb.dao.IncomingFileDao;
import com.clrk.lambda.trydynamodb.dao.TelephoneBookDao;
import com.clrk.lambda.trydynamodb.dto.ErrorLog;
import com.clrk.lambda.trydynamodb.dto.IncomingFile;
import com.clrk.lambda.trydynamodb.dto.Status;
import com.clrk.lambda.trydynamodb.dto.TelephoneRecord;
import com.clrk.lambda.trydynamodb.parser.XlsParser;
import com.clrk.lambda.trydynamodb.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TelephoneBookImporter {

    private final S3Service s3Service;
    private final XlsParser xlsParser;
    private final TelephoneBookDao telephoneBookDao;
    private final IncomingFileDao incomingFileDao;
    private final ErrorLogDao errorLogDao;

    @SneakyThrows
    public void importTelephoneBook(String bucket, String key) {
        final LocalDateTime startDate = getLocalDateTime();
        IncomingFile incomingFile = IncomingFile.builder()
                .fileName(parseFileName(key))
                .createdDate(startDate)
                .status(Status.FOUND)
                .build();
        incomingFileDao.save(incomingFile);
        IncomingFile updatedStatus;
        ErrorLog errorLog;
        try (InputStream inputStream = s3Service.getObjectContent(bucket, key)) {
            List<TelephoneRecord> telephoneRecords = xlsParser.parse(inputStream, key);
            telephoneBookDao.save(telephoneRecords);
            updatedStatus = incomingFile.toBuilder()
                    .finishDate(getLocalDateTime())
                    .status(Status.PARSED)
                    .build();

        } catch (Exception e) {
            updatedStatus = incomingFile.toBuilder()
                    .finishDate(getLocalDateTime())
                    .status(Status.ERROR_PARSING)
                    .build();

            errorLog = ErrorLog.builder()
                    .name(updatedStatus.getFileName())
                    .statusOfDocument(updatedStatus.getStatus())
                    .errorDescription(e.getMessage())
                    .build();
            errorLogDao.save(errorLog);
        }
        incomingFileDao.update(updatedStatus);
    }

    private LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    private static String parseFileName(String fullKey) {
        final String[] splitForUseFileName = fullKey.split("/");
        final String fileName = splitForUseFileName[splitForUseFileName.length - 1];
        return fileName;
    }
}
