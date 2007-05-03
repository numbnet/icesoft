package com.icesoft.faces.presenter.document.factory;

import java.io.File;
import com.icesoft.faces.presenter.document.UnknownPresentationDocument;
import com.icesoft.faces.presenter.document.ZipPresentationDocument;
import com.icesoft.faces.presenter.document.base.PresentationDocument;
import com.icesoft.faces.presenter.presentation.Presentation;

/**
 * Class used to determine what action should be taken for a specific file type
 */
public class DocumentFactory {
    /**
     * Method to generate a PresentationDocument based on the file value of the
     * passed input file component
     *
     * @param file to create a document for
     * @param parent             presentation
     * @return the created presentation document
     */
    public static PresentationDocument createDocument(File file,
                                                      Presentation parent) {
        if (file.getName().toLowerCase().indexOf(".zip") != -1) {
            return new ZipPresentationDocument(parent);
        } else {
            return new UnknownPresentationDocument(parent);
        }
    }
}