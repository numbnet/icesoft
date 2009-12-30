package org.icepush.samples.icechat.cdi.model;

import javax.enterprise.inject.Model;

import org.icepush.samples.icechat.beans.model.BaseCredentialsBean;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;

@Model
public class CredentialsBean extends BaseCredentialsBean implements ICredentialsBean {}
