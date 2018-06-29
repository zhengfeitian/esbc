###eclipse memory analyzer分析内存泄漏
- 程序：通过不断创建hashmap的item来导致内存泄漏
- 通过mat可以看到内存占用大的没有被引用的object
- leak suspect可以分析具体为哪个线程，object的大小等