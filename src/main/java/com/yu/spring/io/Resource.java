package com.yu.spring.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yuhangbin
 * @date 2022/5/8
 **/
public interface Resource {

	InputStream getInputStream() throws IOException;

}
