package org.motechproject.commcare.tasks.builder;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION1;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION2;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION3;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION4;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION5;

public class FormTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    private FormTriggerBuilder formTriggerBuilder;

    private static final int FORM_PREDEFINED_FIELDS = 7;

    @Test
    public void shouldBuildProperTriggerRequestForCases() {
        initMocks(this);
        when(schemaService.getAllFormSchemas()).thenReturn(DummyCommcareSchema.getForms());
        formTriggerBuilder = new FormTriggerBuilder(schemaService);

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();

        assertFalse(triggers.isEmpty());

        assertEquals(DummyCommcareSchema.getForms().size(), triggers.size());

        for(TriggerEventRequest request : triggers) {

            assertEquals(EventSubjects.FORMS_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case "org.motechproject.commcare.api.forms.form1":
                    assertEquals(2 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form1", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION1));
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION2));
                    break;
                case "org.motechproject.commcare.api.forms.form2":
                    assertEquals(3 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form2", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION3));
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION4));
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION5));
                    break;
                default:
                    fail("Found trigger with incorrect subject: " + subject);
            }
        }
    }

    private boolean hasEventKey(List<EventParameterRequest> eventParameters, String key) {
        for (EventParameterRequest parameter : eventParameters) {
            if (parameter.getEventKey().equals(key)) {
                return true;
            }
        }

        return false;
    }
}
